package com.igor.microcomms.products_api.modules.product.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.igor.microcomms.products_api.modules.product.dto.ProductStockDTO;
import com.igor.microcomms.products_api.modules.product.service.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductStockListener {

    private final ProductService productService;

    @RabbitListener(queues = "${app.rabbit.queue.product-stock}")
    public void processProductStockUpdate(ProductStockDTO productStockDTO) throws JsonProcessingException {
        log.info("Received product stock update: {}", new ObjectMapper().writeValueAsString(productStockDTO));
        productService.updateProductStock(productStockDTO);
    }
}
