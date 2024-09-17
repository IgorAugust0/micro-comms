import express from "express";
import { connectToMongoDB } from "./config/db/mongo-db-config.ts";
import { createPlaceholderData } from "./config/db/placeholder-data.ts";
import { connectRabbitMq } from "./config/rabbitmq/rabbit-config.ts";
import middleware from "./middleware.ts";

const app = express();
const env = process.env.NODE_ENV || "development";
const port = process.env.PORT || 8082;

async function startServer() {
  try {
    app.use(middleware);

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
