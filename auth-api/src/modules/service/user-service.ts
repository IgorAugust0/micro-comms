import userRepository from "../repository/user-repository.ts";
import UserException from "../exception/user-exception.ts";
import { StatusCodes } from "http-status-codes";
import { Request } from "express";
import { getErrorMessage } from "../../lib/util.ts";
import { User } from "../model/user-model.ts";

class UserService {
  validateRequestData(email: string) {
    if (!email) {
      throw new UserException(StatusCodes.BAD_REQUEST, "Email is required");
    }
  }

  checkUserExists(user: User | null): asserts user is User {
    if (!user) {
      throw new UserException(StatusCodes.NOT_FOUND, "User not found");
    }
  }

  handleError(err: unknown) {
    return {
      status: StatusCodes.BAD_REQUEST,
      message: getErrorMessage(err),
    };
  }

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
}

export default new UserService();
