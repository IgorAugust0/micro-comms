import { User } from "../../modules/model/user-model.ts";

// Augment the Express.Request object with a custom property in the global scope.
// This is because TypeScript's type system needs to know about the additional property on the Request object.
// ref: https://blog.logrocket.com/extend-express-request-object-typescript/
declare global {
  namespace Express {
    interface Request {
      authUser?: User;
    }
  }
}
