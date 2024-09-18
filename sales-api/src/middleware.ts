import jwt, { JwtPayload } from "jsonwebtoken";
import { ACCESS_TOKEN_SECRET, handleError } from "./lib/util.ts";
import { Request, Response, NextFunction } from "express";
import { OrderException } from "./lib/exceptions.ts";
import { StatusCodes } from "http-status-codes";

// import the User model from the auth api. However this is not the best approach because in a microservices architecture, each service should be independent and not rely on other services. Instead, you should use a shared library or a shared database to share models between services.
import { User } from "../../auth-api/src/modules/model/user-model.ts";

// Define the shape of the decoded JWT payload
interface DecodedToken extends JwtPayload {
  // authUser: { id: number; email: string; role: string };
  authUser: User;
}

export default async (req: Request, res: Response, next: NextFunction) => {
  try {
    const authHeader = req.headers.authorization;
    if (!authHeader || !authHeader.startsWith("Bearer ")) {
      throw new OrderException(StatusCodes.UNAUTHORIZED, "No token provided");
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
