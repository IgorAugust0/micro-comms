// Definition of types used in the project

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
