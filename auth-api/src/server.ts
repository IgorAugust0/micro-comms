import dotenv from "dotenv";
import express from "express";
import { createPlaceholderData as db } from "./lib/placeholder-data.ts";
import { Request, Response } from "express";
import userRouter from "./routes/user-route.ts";
import { StatusCodes } from "http-status-codes";

import middleware from "./middleware.ts";

dotenv.config();

const app = express();
const env = process.env.NODE_ENV || "development";
const port = process.env.PORT || 8080;

db();

app.use(express.json());
app.use(userRouter);
app.use(middleware);

app.get("/api/status", (req: Request, res: Response) => {
  return res
    .status(StatusCodes.OK)
    .json({ service: "Auth-API", status: "up", httpStatus: 200 });
});

app.listen(port, () => {
  console.log(`Server is running on port ${port} in ${env} mode`);
});
