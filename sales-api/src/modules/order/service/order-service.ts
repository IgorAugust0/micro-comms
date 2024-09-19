import { rabbitMQService } from "../../../config/rabbitmq/rabbit-service.ts";
import { StatusCodes } from "http-status-codes";
import { OrderException } from "../../../lib/exceptions";
import { User } from "../../../../../auth-api/src/modules/model/user-model.ts";
import { APPROVED, PENDING, REJECTED } from "../../../lib/status.ts";
import OrderRepository from "../repository/order-repository";
import ProductClient from "../../product/client/product-client.ts";
import type {
  IOrder,
  IProduct,
  OrderResponse,
  StockCheckRequest,
  AuthenticatedRequest,
} from "../../../types/types";

const { OK, CREATED, INTERNAL_SERVER_ERROR, BAD_REQUEST } = StatusCodes;

class OrderService {
  constructor(
    private orderRepository: OrderRepository,
    private productClient: ProductClient
  ) {}

  async createOrder(req: AuthenticatedRequest): Promise<OrderResponse> {
    const {
      body: orderData,
      headers: { transactionId, serviceId, authorization },
      authUser,
    } = req;
    console.info(
      `Request to POST new order with data ${JSON.stringify(
        orderData
      )} | [transactionID: ${transactionId} | serviceID: ${serviceId}]`
    );

    try {
      this.validateOrderData(orderData);

      const order = this.createOrderObject(orderData, authUser);
      await this.validateProductStock({
        order,
        token: authorization,
        transactionId,
      });
      const createdOrder = await this.orderRepository.save(order);

      if (!createdOrder) {
        throw new OrderException(
          INTERNAL_SERVER_ERROR,
          "Failed to create order."
        );
      }
      this.sendMessage(createdOrder);

      return { status: CREATED, createdOrder };
    } catch (err: unknown) {
      return this.handleError(err);
    }
  }

  async updateOrder(orderMessage: string): Promise<void> {
    try {
      const order = JSON.parse(orderMessage) as Partial<IOrder>;
      if (order.id && order.status) {
        const existingOrder = await this.orderRepository.findById(order.id);
        if (existingOrder && existingOrder.status !== order.status) {
          existingOrder.status = order.status;
          await this.orderRepository.save(existingOrder);
        }
      } else {
        console.warn("Invalid order message received: Missing id or status");
      }
    } catch (error) {
      const message = error instanceof Error ? error.message : String(error);
      console.error("Could not parse order message from queue:", message);
    }
  }

  async findAll(): Promise<OrderResponse> {
    try {
      const orders = await this.orderRepository.findAll();
      if (!orders.length) {
        throw new OrderException(BAD_REQUEST, "No orders found.");
      }
      return { status: OK, orders }; // name can be omitted if key and value are the same
    } catch (err: unknown) {
      return this.handleError(err);
    }
  }

  private createOrderObject(
    orderData: { products: IProduct[] },
    authUser: User /*, transactionid: string, serviceid: string*/
  ): Partial<IOrder> {
    return {
      status: PENDING,
      products: orderData.products,
      user: authUser,
      // transactionid,
      // serviceid,
    };
  }

  private sendMessage(createdOrder: IOrder): void {
    // send single message for entire order
    const message = {
      salesId: createdOrder.id,
      products: createdOrder.products,
      productId: createdOrder.products[0].productId,
      quantity: createdOrder.products[0].quantity,
    };
    // send one message per product in the order
    // const message = createdOrder.products.map((product) => ({
    //   salesId: createdOrder.id,
    //   products: createdOrder.products,
    //   productId: product.productId,
    //   quantity: product.quantity,
    // }));
    rabbitMQService.sendStockUpdateMessages([message]);
  }

  private async validateProductStock({
    order,
    token,
    transactionId,
  }: StockCheckRequest): Promise<void> {
    const isStockAvailable = await this.productClient.checkProductStock({
      order,
      token,
      transactionId,
    });

    if (!isStockAvailable) {
      throw new OrderException(BAD_REQUEST, "Insufficient stock for order.");
    }
  }

  private validateOrderData(data: { products?: IProduct[] }): void {
    if (!data?.products) {
      throw new OrderException(BAD_REQUEST, "No products provided for order.");
    }
  }

  private validateInformedId(id: string): void {
    if (!id) {
      throw new OrderException(BAD_REQUEST, "The order's id must be informed.");
    }
  }

  private validateInformedProductId(id: number | string): void {
    if (!id) {
      throw new OrderException(
        BAD_REQUEST,
        "The order's productId must be informed."
      );
    }
  }

  private handleError(err: unknown): OrderResponse {
    const status =
      err instanceof OrderException ? err.status : INTERNAL_SERVER_ERROR;
    const message = err instanceof Error ? err.message : String(err);
    return { status, message };
  }
}

const orderRepository = new OrderRepository();
const productClient = new ProductClient();
const orderService = new OrderService(orderRepository, productClient);
export default orderService;
