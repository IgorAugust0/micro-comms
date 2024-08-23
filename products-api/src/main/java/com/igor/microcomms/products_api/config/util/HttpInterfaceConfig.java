package com.igor.microcomms.products_api.config.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.igor.microcomms.products_api.modules.sales.client.SalesClient;

@Configuration
public class HttpInterfaceConfig {

    @Value("${app.services.sales.url}")
    private String baseUrl;

    @Bean
    public SalesClient salesClient() {
        // setup web client
        var webClient = WebClient.builder().baseUrl(baseUrl).build();
        // wrap web client into adapter
        var webClientAdapter = WebClientAdapter.create(webClient);
        // create proxy factory
        var proxyFactory = HttpServiceProxyFactory.builderFor(webClientAdapter).build();
        // create client by generating a proxy instance of the interface
        var client = proxyFactory.createClient(SalesClient.class);
        return client;
    }
}
