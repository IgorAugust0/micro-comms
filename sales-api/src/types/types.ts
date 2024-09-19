// Definition of types used in the project

import { Document } from "mongoose";
import { Request } from "express";
import { User } from "../../../auth-api/src/modules/model/user-model";

// Order model definition
export interface IProduct {
  productId: number;
  quantity: number;
}

export interface IUser {
  id: number;
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

export interface OrderResponse {
  status: number;
  message?: string;
  createdOrder?: IOrder;
  existingOrder?: IOrder;
  orders?: IOrder[];
  salesIds?: string[];
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
  productId?: number;
  quantity?: number;
  salesId?: string;
  products?: IProduct[];
}

// Message to be sent to the sales confirmation queue
export interface OrderStockUpdateMessage {
  salesId: string;
  products: IProduct[];
}

// Product API request
export interface ProductApiRequest {
  url: string;
  data: { products: IProduct[] };
  headers: Record<string, string>;
}

// Stock check request
export interface StockCheckRequest {
  order: Partial<IOrder>;
  token: string;
  transactionId: string;
}

// Request with authenticated user
export interface AuthenticatedRequest extends Request {
  body: { products: IProduct[] };
  authUser: User;
  headers: {
    transactionId: string;
    serviceId: string;
    authorization: string;
  };
}
