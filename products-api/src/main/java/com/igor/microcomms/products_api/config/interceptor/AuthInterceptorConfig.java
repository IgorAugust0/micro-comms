package com.igor.microcomms.products_api.config.interceptor;

import org.springframework.context.annotation.Bean;

public interface AuthInterceptorConfig extends JWTServiceConfig {

    @Bean
    default AuthInterceptor authInterceptor() {
        return new AuthInterceptor(jwtService());
    }

}
