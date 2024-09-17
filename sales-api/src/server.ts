import express from "express";
import { connectToMongoDB } from "./config/db/mongo-db-config.ts";
import { createPlaceholderData } from "./config/db/placeholder-data.ts";
import { connectRabbitMq } from "./config/rabbitmq/rabbit-config.ts";
import middleware from "./middleware.ts";
import { sendMessageToProductStockUpdateQueue } from "./modules/product/rabbitmq/product-stock-update-sender.ts";

const app = express();
const env = process.env.NODE_ENV || "development";
const port = process.env.PORT || 8082;

async function startServer() {
  try {
    app.use(middleware);

    // testing purposes
    app.get("/test", (req, res) => {
      try {
        sendMessageToProductStockUpdateQueue([
          {
            productId: 1,
            quantity: 2,
          },
          {
            productId: 2,
            quantity: 1,
          },
          {
            productId: 3,
            quantity: 4,
          }
        ]);
        return res.status(200).json({ status: 200 });
      } catch (error) {
        console.log(error);
        return res.status(500).json({ error: true });
      }
    });

    app.get("/api/status", (req, res) => {
      res
        .status(200)
        .json({ service: "Sales-API", status: "up", httpStatus: 200 });
    });

    app.listen(port, () => {
      console.log(`Server is running on port ${port} in ${env} mode`);
    });

    await connectToMongoDB();
    await createPlaceholderData();
    await connectRabbitMq();
  } catch (error) {
    console.error("Failed to start server:", error);
    process.exit(1);
  }
}

startServer();
