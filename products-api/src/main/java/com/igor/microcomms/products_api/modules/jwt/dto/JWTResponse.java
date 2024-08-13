package com.igor.microcomms.products_api.modules.jwt.dto;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JWTResponse {

    private Integer id;
    private String name;
    private String email;

    // with jjwt
    public static JWTResponse getUser(Claims claims) {
        try {
            return new ObjectMapper().convertValue(claims.get("authUser"), JWTResponse.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // with auth0 jwt
    public static JWTResponse getUser(DecodedJWT jwt) {
        try {
            var claims = jwt.getClaim("authUser").asMap();
            return new ObjectMapper().convertValue(claims, JWTResponse.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
