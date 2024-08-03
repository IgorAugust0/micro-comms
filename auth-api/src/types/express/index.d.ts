import { User } from "../../modules/model/user-model.ts";

// Augment the Express types in the global scope
// ref: https://blog.logrocket.com/extend-express-request-object-typescript/
declare global {
  namespace Express {
    interface Request {
      authUser?: User;
    }
  }
}
