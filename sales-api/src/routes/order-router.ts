import { Router } from "express";
import middleware from "../middleware.ts";
import orderController from "../modules/order/controller/order-controller.ts";
import { AuthenticatedRequest } from "../types/types.ts";

const router = Router();

router.post("/api/order/create", (req, res) => {
  return orderController.createOrder(req as AuthenticatedRequest, res);
});
router.get("/api/order/:id", middleware, (req, res) => {
  return orderController.findById(req as AuthenticatedRequest, res);
});

export default router;
