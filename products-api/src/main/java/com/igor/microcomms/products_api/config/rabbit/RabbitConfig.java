package com.igor.microcomms.products_api.config.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class RabbitConfig {

    private final RabbitProperties rabbitProperties;

    @Bean
    public TopicExchange productTopicExchange() {
        return new TopicExchange(rabbitProperties.getExchange().getProduct());
    }

    @Bean
    public Queue productStockQueue() {
        return new Queue(rabbitProperties.getQueue().getProductStock(), true);
    }

    @Bean
    public Queue salesConfirmationQueue() {
        return new Queue(rabbitProperties.getQueue().getSalesConfirmation(), true);
    }

    @Bean
    // redirects the messages from the exchange to the queues
    public Binding productStockQueueBinding(TopicExchange topicExchange) {
        return BindingBuilder
                .bind(productStockQueue()) // this queue is binded
                .to(topicExchange) // to this exchange
                .with(rabbitProperties.getRoutingKey().getProductStock()); // with this key
    }

    @Bean
    public Binding salesConfirmationQueueBinding(TopicExchange topicExchange) {
        return BindingBuilder
                .bind(salesConfirmationQueue())
                .to(topicExchange)
                .with(rabbitProperties.getRoutingKey().getSalesConfirmation());
    }

    // Converts messages to JSON format using the Jackson library and JSON messages to Java objects
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
