import bcrypt from "bcrypt";
import jwt from "jsonwebtoken";
import userRepository from "../repository/user-repository.ts";
import { UserException } from "../../lib/exceptions.ts";
import { Request } from "express";
import { StatusCodes } from "http-status-codes";
import { User } from "../model/user-model.ts";
import { ACCESS_TOKEN_SECRET, handleError } from "../../lib/util.ts";

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

  // ----------- DON'T USE IT IN PRODUCTION -----------
  private static validateEmail(email: string) {
    if (!email) {
      throw new UserException(StatusCodes.BAD_REQUEST, "Email is required");
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

  private static validateAuthUser(user: User, authUser: User | undefined) {
    if (!authUser || user.id !== authUser.id) {
      throw new UserException(
        StatusCodes.FORBIDDEN,
        "You are not authorized to perform this action"
      );
    }
  }

  // Find a user by email
  async findUserByEmail(req: Request) {
    const { email } = req.params;
    const { authUser } = req;

    try {
      // UserService.validateFields(email);
      UserService.validateEmail(email);
      const user = await userRepository.findUserByEmail(email);
      UserService.ensureUserExists(user);
      UserService.validateAuthUser(user, authUser);

      return {
        status: StatusCodes.OK,
        user: {
          id: user.id,
          name: user.name,
          email: user.email,
        },
      };
    } catch (err: unknown) {
      return handleError(err, StatusCodes.BAD_REQUEST);
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
