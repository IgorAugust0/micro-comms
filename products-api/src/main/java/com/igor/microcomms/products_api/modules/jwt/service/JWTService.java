package com.igor.microcomms.products_api.modules.jwt.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
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

    @Value("${app.jwt.key}")
    private String secretKey;

    // jjwt
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

    // auth0
    public String validateTokenAuth0(String token) {
        var accessToken = retrieveToken(token);
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        try {
            var subject = JWT
                    .require(algorithm)
                    .build()
                    .verify(accessToken)
                    .getSubject();
            var decodedJWT = JWT.decode(accessToken);
            var user = JWTResponse.getUser(decodedJWT);
            validateUserAndIdNotEmpty(user);
            return subject;
        } catch (JWTVerificationException ex) {
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
