package com.igor.microcomms.products_api.config.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.igor.microcomms.products_api.config.exception.ValidationException;

import jakarta.servlet.http.HttpServletRequest;

public class RequestUtil {

    public static HttpServletRequest getCurrentRequest() {
        try {
            var attributes = RequestContextHolder.currentRequestAttributes();
            return ((ServletRequestAttributes) attributes).getRequest();
        } catch (Exception e) {
            throw new ValidationException("Failed to get current request");
        }
    }

}
