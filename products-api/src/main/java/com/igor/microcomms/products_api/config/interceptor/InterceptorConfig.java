package com.igor.microcomms.products_api.config.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer, AuthInterceptorConfig {

    // ref: https://docs.spring.io/spring-framework/reference/core/beans/java/bean-annotation.html#beans-java-declaring-a-bean
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor());
    }
}
