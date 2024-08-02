import { Request, Response } from "express";
import userService from "../service/user-service.ts";

class UserController {
  // Method to get an access token
  async getAccessToken(req: Request, res: Response) {
    const accessToken = await userService.getAccessToken(req);
    return res.status(accessToken.status).json(accessToken);
  }
  // Method to find a user by email
  async findUserByEmail(req: Request, res: Response) {
    const foundUser = await userService.findUserByEmail(req);
    return res.status(foundUser.status).json(foundUser);
  }
}

export default new UserController();
