package com.igor.microcomms.products_api.config.interceptor;

import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.igor.microcomms.products_api.modules.jwt.service.JWTService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JWTService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (isOptionsRequest(request)) {
            return true;
        }

        var authorization = request.getHeader("Authorization");
        jwtService.validateToken(authorization); // comment to temporarily disable validation
        // jwtService.validateTokenAuth0(authorization);
        return true;
    }

    private boolean isOptionsRequest(HttpServletRequest request) {
        return HttpMethod.OPTIONS.matches(request.getMethod());
    }
}
