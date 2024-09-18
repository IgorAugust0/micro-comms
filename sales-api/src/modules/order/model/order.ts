import mongoose, { Schema } from "mongoose";
import { IOrder } from "../../../types/types";

const orderSchema = new Schema(
  {
    products: {
      type: [
        {
          productId: Number,
          quantity: Number,
        },
      ],
      required: true,
    },
    user: {
      type: {
        id: String,
        name: String,
        email: String,
      },
      required: true,
    },
    status: {
      type: String,
      required: true,
    },
    createdAt: {
      type: Date,
      required: true,
      default: Date.now,
    },
    updatedAt: {
      type: Date,
      required: true,
      default: Date.now,
    },
  },
  { timestamps: true } // automatically handles createdAt and updatedAt
);

const order = mongoose.model<IOrder>("Order", orderSchema);

export default order;
