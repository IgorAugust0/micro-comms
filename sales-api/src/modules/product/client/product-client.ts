import axios, { AxiosError } from "axios";
import { PRODUCT_API_URL } from "../../../lib/util";
import type {
  ProductApiRequest,
  StockCheckRequest,
} from "../../../types/types";

export default class ProductClient {
  private async makeRequest({ url, data, headers }: ProductApiRequest): Promise<boolean> {
    try {
      await axios.post(url, data, { headers });
      return true;
    } catch (error) {
      if (error instanceof AxiosError) {
        const message = `${error.response?.status} ${error.response?.statusText}`;
        console.error(`Error making request to ${url}: ${message}`);
      } else {
        const message = error instanceof Error ? error.message : String(error);
        console.error(`Unexpected error: ${message}`);
      }
      return false;
    }
  }

  async checkProductStock({ order, token, transactionId }: StockCheckRequest): Promise<boolean> {
    if (!order.products) {
      console.error("No products provided for stock check");
      return false;
    }

    const url = `${PRODUCT_API_URL}check-stock`;
    const data = { products: order.products };
    const headers = {
      Authorization: token,
      transactionId: transactionId,
    };

    console.info(
      `Sending request to Product API. TransactionID: ${transactionId}`
    );

    return this.makeRequest({ url, data, headers });
  }
}
