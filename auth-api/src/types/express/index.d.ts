import { User } from "../../modules/model/user-model.ts";

declare global {
  namespace Express {
    interface Request {
      authUser?: User;
    }
  }
}
