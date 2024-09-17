import amqp, { Connection, Channel } from "amqplib/callback_api";

import {
  PRODUCT_TOPIC,
  PRODUCT_STOCK_UPDATE_QUEUE,
  PRODUCT_STOCK_UPDATE_ROUTING_KEY,
  SALES_CONFIRMATION_QUEUE,
  SALES_CONFIRMATION_ROUTING_KEY,
} from "./queue.ts";

import { RABBITMQ_URL } from "../../lib/util.ts";

const TWO_SECONDS = 2000;

export async function connectRabbitMq() {
  amqp.connect(RABBITMQ_URL, (error, connection) => {
    if (error) {
      throw error;
    }
    createQueue(
      connection,
      PRODUCT_STOCK_UPDATE_QUEUE,
      PRODUCT_STOCK_UPDATE_ROUTING_KEY,
      PRODUCT_TOPIC
    );
    createQueue(
      connection,
      SALES_CONFIRMATION_QUEUE,
      SALES_CONFIRMATION_ROUTING_KEY,
      PRODUCT_TOPIC
    );
    setTimeout(() => {
      connection.close();
    }, TWO_SECONDS);
  });
}

function createQueue(
  connection: Connection,
  queue: string,
  routingKey: string,
  topic: string
): void {
  connection.createChannel((error: Error | null, channel: Channel) => {
    if (error) {
      console.log("Failed to create channel: ", error);
      return;
    }
    channel.assertExchange(topic, "topic", { durable: true });
    channel.assertQueue(queue, { durable: true });
    channel.bindQueue(queue, topic, routingKey);
  });
}
