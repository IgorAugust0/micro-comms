import express from "express";
import { connectToMongoDB } from "./config/db/mongo-db-config.ts";
import Order from "./modules/sales/model/order";

const app = express();
const env = process.env.NODE_ENV || "development";
const port = process.env.PORT || 8082;

async function startServer() {
  try {
    await connectToMongoDB();

    app.get("/api/status", async (req, res) => {
      try {
        const orders = await Order.find().lean().exec();
        console.log("Orders:", orders);

        res
          .status(200)
          .json({ service: "Sales-API", status: "up", httpStatus: 200 });
      } catch (error) {
        console.error("Error fetching orders:", error);
        res
          .status(500)
          .json({ service: "Sales-API", status: "error", httpStatus: 500 });
      }
    });

    app.listen(port, () => {
      console.log(`Server is running on port ${port} in ${env} mode`);
    });
  } catch (error) {
    console.error("Failed to start server:", error);
    process.exit(1);
  }
}

startServer();
