package com.igor.microcomms.products_api.config.interceptor;

import org.springframework.context.annotation.Bean;

import com.igor.microcomms.products_api.modules.jwt.service.JWTService;

public interface JWTServiceConfig {

    @Bean
    default JWTService jwtService() {
        return new JWTService();
    }

}
