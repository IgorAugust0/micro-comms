package com.igor.microcomms.products_api.modules.jwt.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.igor.microcomms.products_api.config.exception.AuthenticationException;
import com.igor.microcomms.products_api.config.util.ResponseUtil.ExceptionType;
import com.igor.microcomms.products_api.modules.jwt.dto.JWTResponse;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import static com.igor.microcomms.products_api.config.util.ResponseUtil.validateNotEmpty;

@Service
public class JWTService {

    private static final String EMPTY_SPACE = " ";
    private static final Integer TOKEN_INDEX = 1;

    @Value("${security.token.secret}")
    private String secretKey;

    public void validateToken(String token) {
        System.out.println("Secret key: " + secretKey); // for testing purposes only
        var accessToken = retrieveToken(token);
        var key = Keys.hmacShaKeyFor(secretKey.getBytes());

        try {
            var claims = Jwts
                    .parser() // parserBuilder()
                    .verifyWith(key) // setSigningKey(key)
                    .build()
                    .parseSignedClaims(accessToken) // parseClaimsJws(accessToken)
                    .getPayload(); // getBody()
            var user = JWTResponse.getUser(claims);
            validateUserAndIdNotEmpty(user);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new AuthenticationException("Token verification failed");
        }
    }

    private String retrieveToken(String token) {
        validateNotEmpty(token, "Token not provided", ExceptionType.AUTHENTICATION);
        return token.contains(EMPTY_SPACE) ? token.split(EMPTY_SPACE)[TOKEN_INDEX] : token;
    }

    private void validateUserAndIdNotEmpty(JWTResponse user) {
        validateNotEmpty(user, "User cannot be empty", ExceptionType.AUTHENTICATION);
        validateNotEmpty(user.getId(), "User ID cannot be empty", ExceptionType.AUTHENTICATION);
    }
}
