import userRepository from "../repository/user-repository.ts";
import UserException from "../exception/user-exception.ts";
import { StatusCodes } from "http-status-codes";
import { Request } from "express";
import { getEnvVariable, handleError } from "../../lib/util.ts";
import { User } from "../model/user-model.ts";
import bcrypt from "bcrypt";
import jwt from "jsonwebtoken";

class UserService {
  // Method to validate if the required fields are present
  private static validateFields(email?: string, password?: string) {
    if (!email || !password) {
      throw new UserException(
        StatusCodes.BAD_REQUEST,
        `${!email ? "Email" : ""}${!email && !password ? " and " : ""}${!password ? "Password" : ""} ${!email && !password ? "are" : "is"} required`
      );
    }
  }

  // Method to ensure the user exists
  private static ensureUserExists(user: User | null): asserts user is User {
    if (!user) {
      throw new UserException(StatusCodes.NOT_FOUND, "User not found");
    }
  }

  // Method to validate passwords
  private static async validatePassword(password: string, hashedPwrd: string) {
    const isValid = await bcrypt.compare(password, hashedPwrd);
    if (!isValid) {
      throw new UserException(StatusCodes.UNAUTHORIZED, "Invalid password");
    }
  }

  // Find a user by email
  async findUserByEmail(req: Request) {
    const { email } = req.params;

    try {
      UserService.validateFields(email);
      const user = await userRepository.findUserByEmail(email);
      UserService.ensureUserExists(user);

      return {
        status: StatusCodes.OK,
        user: {
          id: user.id,
          name: user.name,
          email: user.email,
        },
      };
    } catch (err: unknown) {
      return handleError(err);
    }
  }

  // Get an access token for user
  async getAccessToken(req: Request) {
    const { email, password } = req.body;

    try {
      UserService.validateFields(email, password);
      const user = await userRepository.findUserByEmail(email);
      UserService.ensureUserExists(user);
      await UserService.validatePassword(password, user.password);

      const ACCESS_TOKEN_SECRET = getEnvVariable("ACCESS_TOKEN_SECRET");

      const authUser = { id: user.id, name: user.name, email: user.email };
      const accessToken = jwt.sign({ authUser }, ACCESS_TOKEN_SECRET, {
        expiresIn: "1d",
      });

      return {
        status: StatusCodes.OK,
        message: "Access token generated",
        accessToken,
      };
    } catch (err: unknown) {
      return handleError(err);
    }
  }
}

export default new UserService();
