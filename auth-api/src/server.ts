import express from "express";
import { createPlaceholderData as db } from "./lib/placeholder-data.ts";
import { Request, Response } from "express";
import userRouter from "./routes/user-route.ts";

const app = express();
const env = process.env.NODE_ENV || "development";
const port = process.env.PORT || 8080;

db();

app.use(express.json());
app.use(userRouter);

app.get("/api/status", (req: Request, res: Response) => {
  return res
    .status(200)
    .json({ service: "Auth-API", status: "up", httpStatus: 200 });
});

app.listen(port, () => {
  console.log(`Server is running on port ${port} in ${env} mode`);
});
