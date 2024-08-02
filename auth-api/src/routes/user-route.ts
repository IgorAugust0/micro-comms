import { Router } from "express";
import userController from "../modules/controller/user-controller.ts";

const router = Router();

router.get("/api/user/email/:email", userController.findUserByEmail);
router.post("/api/user/auth", userController.getAccessToken);

export default router;
