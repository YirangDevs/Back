package com.api.yirang.auth.domain.jwt.components;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:properties/application-jwt.properties")
public class JwtValidator {

    private final String JWT_SECRET;

    public JwtValidator(@Value("${spring.app.jwtSecret}") String JWT_SECRET) {
        this.JWT_SECRET = JWT_SECRET;
    }

    public boolean isValidJwt(String token){
        try {
            Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
}
