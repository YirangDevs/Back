package com.api.yirang.auth.domain.jwt.components;


import com.api.yirang.auth.domain.jwt.exceptions.InvalidJwtException;
import io.jsonwebtoken.Jwts;
import com.api.yirang.auth.support.type.Authority;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@PropertySource("classpath:properties/application-jwt.properties")
public class JwtParser {

    private final String JWT_SECRET;

    public JwtParser(@Value("${spring.app.jwtSecret}") String JWT_SECRET) {
        this.JWT_SECRET = JWT_SECRET;
    }

    public Authority getRoleFromJwt(String token) {
        try {
            String authority = (String) Jwts.parser()
                                            .setSigningKey(JWT_SECRET)
                                            .parseClaimsJws(token)
                                            .getBody()
                                            .get("role");

            return authority.equals("USER") ? Authority.ROLE_USER : Authority.ROLE_ADMIN;
        }
        catch (Exception ex){
            throw new InvalidJwtException();
        }
    }

    public Long getUserIdFromJwt(String token){
        try {
            return Long.parseLong(
                    (String) Jwts.parser()
                                 .setSigningKey(JWT_SECRET)
                                 .parseClaimsJws(token)
                                 .getBody()
                                 .get("userId")
            );
        }
        catch (Exception ex){
            throw new InvalidJwtException();
        }
    }

}
