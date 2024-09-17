import Order from "../../modules/sales/model/order.ts";

export async function createPlaceholderData() {
  await Order.collection.drop();
  await Order.create({
    products: [
      {
        productId: 1,
        quantity: 2,
      },
      {
        productId: 2,
        quantity: 1,
      },
      {
        productId: 3,
        quantity: 3,
      },
    ],
    user: {
      id: "a8s7a9d8a9s8d7a9s8d7",
      name: "John Doe",
      email: "johndoe@email.com",
    },
    status: "APPROVED",
    createdAt: new Date(),
    updatedAt: new Date(),
  });
  await Order.create({
    products: [
      {
        productId: 4,
        quantity: 1,
      },
      {
        productId: 5,
        quantity: 2,
      },
    ],
    user: {
      id: "j2da9s8d9a8s7d9a8s7d",
      name: "Jane Doe",
      email: "janedoe@email.com",
    },
    status: "REJECTED",
    createdAt: new Date(),
    updatedAt: new Date(),
  });
  console.log("Placeholder data created");
  const orders = await Order.find();
  console.log("Orders:", orders);
}
