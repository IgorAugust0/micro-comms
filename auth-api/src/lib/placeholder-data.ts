import bcrypt from "bcrypt";
import user from "../modules/model/user-model.ts";

export async function createPlaceholderData() {
  try {
    await user.sync({ force: true });

    const hashedPassword = await bcrypt.hash("password", 10);

    await user.create({
      name: "John Doe",
      email: "johndoe@email.com",
      password: hashedPassword,
    });
  } catch (error) {
    console.error(error);
  }
}
