import { StatusCodes } from "http-status-codes";
import { BaseException } from "./exceptions.ts";
import dotenv from "dotenv";

dotenv.config();

// get error message from error object
export const getErrorMessage = (error: unknown): string => {
  if (error instanceof Error) {
    return error.message;
  } else if (error && typeof error === "object" && "message" in error) {
    return String(error.message);
  } else if (typeof error === "string") {
    return error;
  } else {
    return "Something went wrong";
  }
};

// handle errors and return the status code and message
export const handleError = (err: unknown, customStatusCode?: number) => {
  const status =
    err instanceof BaseException
      ? err.status
      : (customStatusCode ?? StatusCodes.INTERNAL_SERVER_ERROR);
  const message = getErrorMessage(err);
  return { status, message };
};

// get env variable while checking if it exists, providing a default value if it doesn't
export const getEnvVariable = (name: string, defaultValue?: string): string => {
  const value = process.env[name];

  if (!value) {
    if (defaultValue !== undefined) {
      return defaultValue;
    }
    throw new Error(`${name} environment variable is not set`);
  }

  // return value ?? defaultValue;
  console.log("value:", value); // debug
  return value;
};

export const ACCESS_TOKEN_SECRET = getEnvVariable("ACCESS_TOKEN_SECRET");
export const MONGO_DB_URL = getEnvVariable("MONGO_DB_URL", "mongodb://admin:123456@localhost:27017");
export const RABBITMQ_URL = getEnvVariable("RABBITMQ_URL", "amqp://localhost:5672");
