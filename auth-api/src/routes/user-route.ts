import { Router } from "express";
import userController from "../modules/controller/user-controller.ts";
import middleware from "../middleware.ts";

const router = Router();

router.post("/api/user/auth", userController.getAccessToken);
router.use(middleware);
router.get("/api/user/email/:email", userController.findUserByEmail);

export default router;
