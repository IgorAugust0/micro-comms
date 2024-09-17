import amqp, { Connection, Channel } from "amqplib/callback_api";
import config from "./queue.ts";

const TWO_SECONDS = 2000;

interface RabbitMQConfig {
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

export async function connectRabbitMq(): Promise<void> {
  const rabbitConfig: RabbitMQConfig = config.get("rabbitmq");

  amqp.connect(
    rabbitConfig.url,
    (error: Error | null, connection: Connection) => {
      if (error) {
        console.error("Failed to connect to RabbitMQ:", error);
        return;
      }
      createQueue(
        connection,
        rabbitConfig.queues.productStockUpdate,
        rabbitConfig.routingKeys.productStockUpdate,
        rabbitConfig.topics.product
      );
      createQueue(
        connection,
        rabbitConfig.queues.salesConfirmation,
        rabbitConfig.routingKeys.salesConfirmation,
        rabbitConfig.topics.product
      );
      setTimeout(() => {
        connection.close();
      }, TWO_SECONDS);
    }
  );
}

function createQueue(
  connection: Connection,
  queue: string,
  routingKey: string,
  topic: string
): void {
  connection.createChannel((error: Error | null, channel: Channel) => {
    if (error) {
      console.error("Failed to create channel: ", error);
      return;
    }
    channel.assertExchange(topic, "topic", { durable: true });
    channel.assertQueue(queue, { durable: true });
    channel.bindQueue(queue, topic, routingKey);
  });
}
