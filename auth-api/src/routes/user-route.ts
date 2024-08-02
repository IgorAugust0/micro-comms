import { Router } from "express";
import userController from "../modules/controller/user-controller.ts";

const router = Router();

router.get("/api/user/email/:email", userController.findUserByEmail);

export default router;
