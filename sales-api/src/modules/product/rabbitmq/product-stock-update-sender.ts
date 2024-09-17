import amqp, { Connection, Channel, Message } from "amqplib/callback_api";
import config from "../../../config/rabbitmq/queue.ts";
import { RabbitMQConfig } from "../../../config/rabbitmq/rabbit-config.ts";

interface StockUpdateMessage {
  productId: number;
  quantity: number;
}

export function sendMessageToProductStockUpdateQueue(
  message: StockUpdateMessage[]
) {
  const rabbitConfig: RabbitMQConfig = config.get("rabbitmq");

  amqp.connect(
    rabbitConfig.url,
    (error: Error | null, connection: Connection) => {
      if (error) {
        console.error("Failed to connect to RabbitMQ:", error);
        return;
      }

      connection.createChannel((error: Error | null, channel: Channel) => {
        if (error) {
          console.error("Failed to create channel:", error);
          return;
        }

        const jsonMessage = JSON.stringify(message);
        console.info(`Sending message to product stock update: ${jsonMessage}`);

        channel.publish(
          rabbitConfig.topics.product,
          rabbitConfig.routingKeys.productStockUpdate,
          Buffer.from(jsonMessage)
        );
        console.info("Messages were sent successfully!");
      });
    }
  );
}
