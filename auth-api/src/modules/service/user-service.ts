import userRepository from "../repository/user-repository.ts";
import UserException from "../exception/user-exception.ts";
import { StatusCodes } from "http-status-codes";
import { Request } from "express";
import { getEnvVariable, getErrorMessage } from "../../lib/util.ts";
import { User } from "../model/user-model.ts";
import bcrypt from "bcrypt";
import jwt from "jsonwebtoken";

class UserService {
  // Method to validate if the required fields are present
  validateRequestData(email: string) {
    if (!email) {
      throw new UserException(StatusCodes.BAD_REQUEST, "Email is required");
    }
  }

  // Method to ensure the user exists
  checkUserExists(user: User | null): asserts user is User {
    if (!user) {
      throw new UserException(StatusCodes.NOT_FOUND, "User not found");
    }
  }

  // Method to validate passwords
  async validatePassword(password: string, hashedPassword: string) {
    if (!(await bcrypt.compare(password, hashedPassword))) {
      throw new UserException(StatusCodes.UNAUTHORIZED, "Invalid password");
    }
  }

  // Method to handle errors
  handleError(err: unknown) {
    return {
      status: StatusCodes.BAD_REQUEST,
      message: getErrorMessage(err),
    };
  }

  // Method to validate the request data for generating an access token
  validateAccessTokenRequestData(email: string, password: string) {
    if (!email || !password) {
      throw new UserException(
        StatusCodes.UNAUTHORIZED,
        "Email and password are required"
      );
    }
  }

  // Find a user by email
  async findUserByEmail(req: Request) {
    try {
      const { email } = req.params;
      this.validateRequestData(email);
      const user = await userRepository.findUserByEmail(email);
      this.checkUserExists(user);
      return {
        status: StatusCodes.OK,
        user: {
          id: user.id,
          name: user.name,
          email: user.email,
        },
      };
    } catch (err: unknown) {
      return this.handleError(err);
    }
  }

  // Get an access token for user
  async getAccessToken(req: Request) {
    try {
      const { email, password } = req.body;
      this.validateAccessTokenRequestData(email, password);
      const user = await userRepository.findUserByEmail(email);
      this.checkUserExists(user);
      await this.validatePassword(password, user.password);
      const authUser = { id: user.id, name: user.name, email: user.email };
      const ACCESS_TOKEN_SECRET = getEnvVariable("ACCESS_TOKEN_SECRET");
      const accessToken = jwt.sign({ authUser }, ACCESS_TOKEN_SECRET, {
        expiresIn: "1d",
      });
      return {
        status: StatusCodes.OK,
        message: "Access token generated",
        accessToken,
      };
    } catch (err: unknown) {
      return this.handleError(err);
    }
  }
}

export default new UserService();
