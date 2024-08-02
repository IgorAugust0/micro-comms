import userService from "../service/user-service.ts";
import { Request, Response } from "express";

class UserController {
  async getAccessToken(req: Request, res: Response) {
    try {
      const accessToken = await userService.getAccessToken(req);
      return res.status(accessToken.status).json(accessToken);
    } catch (err: unknown) {
      return res.status(400).json(err);
    }
  }
  async findUserByEmail(req: Request, res: Response) {
    try {
      const user = await userService.findUserByEmail(req);
      return res.status(user.status).json(user);
    } catch (err: unknown) {
      return res.status(400).json(err);
    }
  }
}

export default new UserController();
