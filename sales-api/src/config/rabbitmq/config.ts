import dotenv from "dotenv";
import convict from "convict";

dotenv.config();

const config = convict({
  env: {
    doc: "The application environment.",
    format: ["production", "development", "test"],
    default: "development",
    env: "NODE_ENV",
  },
  rabbitmq: {
    url: {
      doc: "RabbitMQ URL",
      format: String,
      default: "amqp://localhost:5672",
      env: "RABBITMQ_URL",
    },
    topics: {
      product: {
        doc: "Product topic name",
        format: String,
        default: "product.topic",
        env: "RABBITMQ_PRODUCT_TOPIC",
      },
    },
    queues: {
      productStockUpdate: {
        doc: "Product stock update queue name",
        format: String,
        default: "product-stock-update.queue",
        env: "RABBITMQ_PRODUCT_STOCK_UPDATE_QUEUE",
      },
      salesConfirmation: {
        doc: "Sales confirmation queue name",
        format: String,
        default: "sales-confirmation.queue",
        env: "RABBITMQ_SALES_CONFIRMATION_QUEUE",
      },
    },
    routingKeys: {
      productStockUpdate: {
        doc: "Product stock update routing key",
        format: String,
        default: "product-stock-update.routingKey",
        env: "RABBITMQ_PRODUCT_STOCK_UPDATE_ROUTING_KEY",
      },
      salesConfirmation: {
        doc: "Sales confirmation routing key",
        format: String,
        default: "sales-confirmation.routingKey",
        env: "RABBITMQ_SALES_CONFIRMATION_ROUTING_KEY",
      },
    },
  },
});

// Perform validation
config.validate({ allowed: "strict" });

export default config;
