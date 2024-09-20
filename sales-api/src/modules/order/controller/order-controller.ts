import { Response } from "express";
import orderService from "../service/order-service";
import { AuthenticatedRequest } from "../../../types/types";

class OrderController {
  async createOrder(req: AuthenticatedRequest, res: Response) {
    try {
      const order = await orderService.createOrder(req);
      return res.status(order.status).json(order);
    } catch (error) {
      console.error("Error creating order:", error);
      return res.status(500).json({ message: "Internal server error" });
    }
  }

  async findById(req: AuthenticatedRequest, res: Response) {
    try {
      const order = await orderService.findById(req);
      return res.status(order.status).json(order);
    } catch (error) {
      console.error("Error finding order:", error);
      return res.status(500).json({ message: "Internal server error" });
    }
  }
}

const orderController = new OrderController();
export default orderController;
