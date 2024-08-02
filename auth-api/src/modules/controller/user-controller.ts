import userService from "../service/user-service.ts";
import { Request, Response } from "express";

class UserController {
  async findUserByEmail(req: Request, res: Response) {
    try {
      const result = await userService.findUserByEmail(req);
      return res.status(result.status).json(result);
    } catch (err: unknown) {
      return res.status(400).json(err);
    }
  }
}

export default new UserController();
