/* eslint-disable @typescript-eslint/no-unused-vars */
import jwt, { JwtPayload } from "jsonwebtoken";
import { promisify } from "util";
import { ACCESS_TOKEN_SECRET, handleError } from "./lib/util.ts";
import { Request, Response, NextFunction } from "express";
import { AuthException, BaseException } from "./lib/exceptions.ts";
import { StatusCodes } from "http-status-codes";
import { User } from "./modules/model/user-model.ts";

// Define the shape of the decoded JWT payload
interface DecodedToken extends JwtPayload {
  authUser: User;
}

export default async (req: Request, res: Response, next: NextFunction) => {
  try {
    const authHeader = req.headers.authorization;
    if (!authHeader || !authHeader.startsWith("Bearer ")) {
      throw new AuthException(StatusCodes.UNAUTHORIZED, "No token provided");
    }
    const token = authHeader.split(" ")[1];

    // verify the token
    const decoded = jwt.verify(token, ACCESS_TOKEN_SECRET) as DecodedToken;

    // attach the user to the request object
    req.authUser = decoded.authUser;

    // move to the next middleware
    return next();
  } catch (err: unknown) {
    const { status, message } = handleError(err);
    return res.status(status).json({ status, message });
  }
};
