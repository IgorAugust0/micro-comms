package com.igor.microcomms.products_api.config.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${app.rabbit.exchange.product}")
    private String productTopicExchange;

    @Value("${app.rabbit.routingKey.product-stock}")
    private String productStockKey;

    @Value("${app.rabbit.routingKey.sales-confirmation}")
    private String salesConfirmationKey;

    @Value("${app.rabbit.queue.product-stock}")
    private String productStockQueue;

    @Value("${app.rabbit.queue.sales-confirmation}")
    private String salesConfirmationQueue;

    @Bean
    public TopicExchange productTopicExchange() {
        return new TopicExchange(productTopicExchange);
    }

    @Bean
    public Queue productStockQueue() {
        return new Queue(productStockQueue, true);
    }

    @Bean
    public Queue salesConfirmationQueue() {
        return new Queue(salesConfirmationQueue, true);
    }

    @Bean
    // redirects the messages from the exchange to the queues
    public Binding productStockMqBinding(TopicExchange topicExchange) {
        return BindingBuilder
                .bind(productStockQueue()) // this queue is binded
                .to(topicExchange) // to this exchange
                .with(productStockKey); // with this key
    }

    @Bean
    public Binding salesConfirmationMqBinding(TopicExchange topicExchange) {
        return BindingBuilder
                .bind(salesConfirmationQueue())
                .to(topicExchange)
                .with(salesConfirmationKey);
    }

    // Converts messages to JSON format using the Jackson library, and can also
    // convert JSON messages to Java objects
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
