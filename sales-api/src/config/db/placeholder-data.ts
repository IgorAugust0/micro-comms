// import Order from "../../modules/order/model/order.ts";
import OrderRepository from "../../modules/order/repository/order-repository.ts";
import { IOrder } from "../../types/types.ts";

const orderRepo = new OrderRepository();

export async function createPlaceholderData(): Promise<void> {
  // Drop the existing collection
  await orderRepo.dropCollection();

  // Create first order
  await orderRepo.save({
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
      id: 1,
      name: "John Doe",
      email: "johndoe@email.com",
    },
    status: "APPROVED",
  } as Partial<IOrder>); // dismises the error if any property is missing

  // Create second order
  await orderRepo.save({
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
      id: 2,
      name: "Jane Doe",
      email: "janedoe@email.com",
    },
    status: "REJECTED",
  } as Partial<IOrder>);

  console.log("Placeholder data created");

  // Fetch and log all orders
  const orders = await orderRepo.findAll();
  console.log("Orders:", orders);
}
