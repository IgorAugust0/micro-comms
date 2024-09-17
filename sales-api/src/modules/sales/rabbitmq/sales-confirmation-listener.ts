import amqp, { Connection, Channel, Message } from "amqplib/callback_api";
import config from "../../../config/rabbitmq/queue.ts";
import { RabbitMQConfig } from "../../../config/rabbitmq/rabbit-config.ts";

export function listenToSalesConfirmationQueue() {
  const rabbitConfig: RabbitMQConfig = config.get("rabbitmq");

  amqp.connect(
    rabbitConfig.url,
    (error: Error | null, connection: Connection) => {
      if (error) {
        console.error("Failed to connect to RabbitMQ:", error);
        return;
      }
      console.info("Listening to Sales Confirmation Queue...");

      connection.createChannel((error: Error | null, channel: Channel) => {
        if (error) {
          console.error("Failed to create channel:", error);
          return;
        }

        channel.consume(
          rabbitConfig.queues.salesConfirmation,
          (message: Message | null) => {
            if (message) {
              const content = message.content.toString();
              console.info(`Receiving message from queue: ${content}`);
            }
          },
          {
            noAck: true, // auto acknowledge so it won't be requeued
          }
        );
      });
    }
  );
}
