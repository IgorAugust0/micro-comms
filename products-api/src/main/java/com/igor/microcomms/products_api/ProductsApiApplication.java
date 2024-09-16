package com.igor.microcomms.products_api;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.igor.microcomms.products_api.config.rabbit.RabbitProperties;

@EnableRabbit
@SpringBootApplication
@EnableConfigurationProperties(RabbitProperties.class)
public class ProductsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductsApiApplication.class, args);
	}

}
