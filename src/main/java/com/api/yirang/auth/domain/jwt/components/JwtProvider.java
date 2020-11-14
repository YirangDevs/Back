package com.api.yirang.auth.domain.jwt.components;

import com.api.yirang.auth.support.type.Authority;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

@Component
@PropertySource("classpath:properties/application-jwt.properties")
public class JwtProvider {

    private final String JWT_SECRET;
    private final int JWT_EXPIRATION_MS;

    public JwtProvider(@Value("${spring.app.jwtSecret}") String JWT_SECRET,
                       @Value("${spring.app.jwtExpirationMs}") int JWT_EXPIRATION_MS ) {
        this.JWT_SECRET = JWT_SECRET;
        this.JWT_EXPIRATION_MS = JWT_EXPIRATION_MS;
    }

    public String generateJwtToken(String username, String imgUrl, Long userId, Authority authority){
        // 예상 되는 문제 Date 와 LocalDateTime 호완성
        return Jwts.builder()
                   // 원래 이거 없었음
                   .claim("username", username)
                   .claim("imgUrl", imgUrl)
                   .claim("userId", userId.toString())
                   .claim("role", authority.toString())
                   .setIssuedAt(new Date() )
                   .setExpiration(new Date( (new Date()).getTime() + JWT_EXPIRATION_MS) )
                   .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                   .compact();
    }

}
