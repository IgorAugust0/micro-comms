// Definition of types used in the project

import { Document } from "mongoose";

// Order model definition
export interface IProduct {
  productId: number;
  quantity: number;
}

export interface IUser {
  id: string;
  name: string;
  email: string;
}

export interface IOrder extends Document {
  products: IProduct[];
  user: IUser;
  status: string;
  createdAt: Date;
  updatedAt: Date;
}

// Configuration for RabbitMQ connection
export interface RabbitMQConfig {
  url: string;
  queues: {
    productStockUpdate: string;
    salesConfirmation: string;
  };
  routingKeys: {
    productStockUpdate: string;
    salesConfirmation: string;
  };
  topics: {
    product: string;
  };
}

// Message to be sent to the product stock update queue
export interface StockUpdateMessage {
  productId: number;
  quantity: number;
}
