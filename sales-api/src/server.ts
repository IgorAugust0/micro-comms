import express from "express";
import middleware from "./middleware.ts";
import { connectToMongoDB } from "./config/db/mongo-db-config.ts";
import { createPlaceholderData } from "./config/db/placeholder-data.ts";
import { rabbitMQService } from "./config/rabbitmq/rabbit-service.ts";
import orderRouter from "./routes/order-router.ts";

const app = express();
const env = process.env.NODE_ENV || "development";
const port = process.env.PORT || 8082;

async function startServer() {
  try {
    app.use(express.json());
    // app.use(middleware);
    app.use(orderRouter);

    // testing purposes
    app.get("/test", async (req, res) => {
      try {
        await rabbitMQService.sendStockUpdateMessages([
          { productId: 1, quantity: 2 },
          { productId: 2, quantity: 1 },
          { productId: 3, quantity: 4 },
        ]);
        return res.status(200).json({ status: 200 });
      } catch (error) {
        console.error("Error sending stock update messages:", error);
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

    // Initialize services
    await connectToMongoDB();
    await createPlaceholderData();
    await rabbitMQService.initialize();

    console.log("Server initialization completed");
  } catch (error) {
    console.error("Failed to start server:", error);
    process.exit(1);
  }
}

process.on("SIGINT", async () => {
  console.log("Gracefully shutting down...");
  await rabbitMQService.close();
  process.exit(0);
});

startServer();
