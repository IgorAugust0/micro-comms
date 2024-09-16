import mongoose from "mongoose";
import { MONGO_DB_URL } from "../../lib/util.ts";

export default async function connectToMongoDB() {
  try {
    await mongoose.connect(MONGO_DB_URL);
    console.info("Connected to MongoDB");
  } catch (error) {
    console.error("Error connecting to MongoDB:", error);
    process.exit(1);
  }

  mongoose.connection.on("error", console.error.bind(console, "MongoDB connection error:"));
}