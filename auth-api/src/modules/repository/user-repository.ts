import user from "../model/user-model.ts";

class UserRepository {
  async createUser(name: string, email: string, password: string) {
    try {
      return await user.create({ name, email, password });
    } catch (error) {
      console.error(error);
    }
  }

  async findUserById(id: number) {
    try {
      return await user.findByPk(id);
    } catch (error) {
      console.error(error);
      return null;
    }
  }

  async findUserByEmail(email: string) {
    try {
      return await user.findOne({ where: { email } });
    } catch (error) {
      console.error(error);
      return null;
    }
  }
}

export default new UserRepository();
