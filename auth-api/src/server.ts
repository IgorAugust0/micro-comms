import express from "express";

const app = express();
const env = process.env.NODE_ENV || "development";
const port = process.env.PORT || 8080;

app.get("/api/status", (req, res) => {
  return res
    .status(200)
    .json({ service: "Auth-API", status: "up", httpStatus: 200 });
});

app.listen(port, () => {
  console.log(`Server is running on port ${port} in ${env} mode`);
});
