package com.igor.microcomms.products_api.modules.sales.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.igor.microcomms.products_api.modules.sales.dto.SalesConfirmationDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SalesConfirmationSender {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbit.exchange.product}")
    private String productTopicExchange;

    @Value("${app.rabbit.routingKey.sales-confirmation}")
    private String salesConfirmationKey;

    /**
     * Sends the sales confirmation message to the exchange with the routing key,
     * which will be redirected to the queue
     */
    public void sendSalesConfirmationMessage(SalesConfirmationDTO message) {
        try {
            log.info("Sending sales confirmation: {}", new ObjectMapper().writeValueAsString(message));
            rabbitTemplate.convertAndSend(productTopicExchange, salesConfirmationKey, message);
            log.info("Message sent successfully");
        } catch (Exception e) {
            log.error("Error sending sales confirmation: {}", e.getMessage());
        }
    }
}
