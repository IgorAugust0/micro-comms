export class BaseException extends Error {
  public status: number;
  constructor(status: number, message: string) {
    super(message);
    this.status = status;
    this.message = message;
    this.name = this.constructor.name;
    Error.captureStackTrace(this, this.constructor);
  }
}

export class UserException extends BaseException {
  constructor(status: number, message: string) {
    super(status, message);
  }
}

export class OrderException extends BaseException {
  constructor(status: number, message: string) {
    super(status, message);
  }
}
