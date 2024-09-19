import { Model } from "mongoose";
import Order from "../model/order.ts";
import { IOrder } from "../../../types/types.ts";

export default class OrderRepository {
  private model: Model<IOrder>;

  constructor(model: Model<IOrder> = Order) {
    this.model = model;
  }

  async save(order: Partial<IOrder>): Promise<IOrder | null> {
    return this.executeQuery(() => this.model.create(order));
  }

  async findById(id: string): Promise<IOrder | null> {
    return this.executeQuery(() => this.model.findById(id));
  }

  async findAll(): Promise<IOrder[]> {
    const result = await this.executeQuery(() => this.model.find());
    return result || []; // '||' = any falsy value, '??' = null or undefined
  }

  async findByProductId(productId: number): Promise<IOrder[]> {
    const result = await this.executeQuery(() =>
      this.model.find({ "products.productId": productId })
    );
    return result || [];
  }

  async dropCollection(): Promise<void> {
    await this.executeQuery(() => this.model.collection.drop());
  }

  private async executeQuery<T>(queryFn: () => Promise<T>): Promise<T | null> {
    try {
      return await queryFn();
    } catch (error) {
      const message = error instanceof Error ? error.message : String(error);
      console.error("Database operation failed:", message);
      return null;
    }
  }
}
