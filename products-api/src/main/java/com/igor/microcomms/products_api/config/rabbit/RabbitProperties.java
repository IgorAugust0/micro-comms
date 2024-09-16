package com.igor.microcomms.products_api.config.rabbit;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.rabbit")
public class RabbitProperties {

    private Exchange exchange;
    private RoutingKey routingKey;
    private Queue queue;

    @Data
    public static class Exchange {
        private String product;
    }

    @Data
    public static class RoutingKey {
        private String productStock;
        private String salesConfirmation;
    }

    @Data
    public static class Queue {
        private String productStock;
        private String salesConfirmation;
    }
}
