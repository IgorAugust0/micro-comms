import amqp, { Connection, Channel, Message } from "amqplib";
import config from "./config.ts";
import { RabbitMQConfig, StockUpdateMessage } from "../../types/types.ts";

class RabbitMQService {
  private connection: Connection | null = null;
  private channel: Channel | null = null;
  private readonly config: RabbitMQConfig;

  constructor() {
    this.config = config.get("rabbitmq") as RabbitMQConfig;
  }

  private async getChannel(): Promise<Channel> {
    if (!this.connection) {
      this.connection = await amqp.connect(this.config.url);
    }
    if (!this.channel) {
      this.channel = await this.connection.createChannel();
    }
    return this.channel;
  }

  async initialize(): Promise<void> {
    const channel = await this.getChannel();
    const { topics, queues, routingKeys } = this.config;

    await channel.assertExchange(topics.product, "topic", { durable: true });
    await channel.assertQueue(queues.productStockUpdate, { durable: true });
    await channel.assertQueue(queues.salesConfirmation, { durable: true });
    await channel.bindQueue(
      queues.productStockUpdate,
      topics.product,
      routingKeys.productStockUpdate
    );
    await channel.bindQueue(
      queues.salesConfirmation,
      topics.product,
      routingKeys.salesConfirmation
    );
    console.info("RabbitMQ initialized successfully");
  }

  async sendStockUpdateMessages(messages: StockUpdateMessage[]): Promise<void> {
    const channel = await this.getChannel();
    const jsonStringMessage = JSON.stringify(messages);
    channel.publish(
      this.config.topics.product,
      this.config.routingKeys.productStockUpdate,
      Buffer.from(jsonStringMessage)
    );
    console.info(`Sent ${messages.length} stock update messages`);
  }

  async listenToSalesConfirmation(
    callback: (content: string) => Promise<void>
  ): Promise<void> {
    const channel = await this.getChannel();
    await channel.consume(
      this.config.queues.salesConfirmation,
      async (message: Message | null) => {
        if (message) {
          const content = message.content.toString();
          try {
            await callback(content);
            channel.ack(message);
          } catch (error) {
            console.error("Error processing message:", error);
            channel.nack(message);
          }
        }
      }
    );
    console.info("Listening to Sales Confirmation Queue");
  }

  async close(): Promise<void> {
    if (this.channel) {
      await this.channel.close();
    }
    if (this.connection) {
      await this.connection.close();
    }
    console.info("RabbitMQ connection closed");
  }
}

export const rabbitMQService = new RabbitMQService();
