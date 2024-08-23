package com.igor.microcomms.products_api.modules.sales.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import com.igor.microcomms.products_api.modules.sales.dto.SalesProductResponse;

import java.util.Optional;

@HttpExchange("/api/orders")
public interface SalesClient {

    @GetExchange("/product/{productId}")
    Optional<SalesProductResponse> findSalesByProductId(
            @PathVariable(value = "productId") Integer productId,
            @RequestHeader(name = "Authorization") String authorization,
            @RequestHeader(name = "transactionId") String transactionId);
}
