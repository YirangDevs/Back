package com.api.yirang.auth.domain.JWT;


import com.api.yirang.auth.domain.jwt.components.JwtParser;
import com.api.yirang.auth.domain.jwt.components.JwtProvider;
import com.api.yirang.auth.support.type.Authority;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JwtTest {

    @Autowired
    JwtParser jwtParser;

    @Autowired
    JwtProvider jwtProvider;

    // Mock variables
    String username = "likemin0142";
    String imageUrl = "123@sdf";
    Long id = Long.valueOf(123);
    Authority authority = Authority.ROLE_ADMIN;

    @Test
    public void JWT_생성_후_파싱(){
        String jwt = jwtProvider.generateJwtToken(username, imageUrl, id, authority);

        System.out.println("JWT: " + jwt);
        assertThat(jwtParser.getUsernameFromJwt(jwt)).isEqualTo(username);
        assertThat(jwtParser.getImageUrlFromJwt(jwt)).isEqualTo(imageUrl);
        assertThat(jwtParser.getUserIdFromJwt(jwt)).isEqualTo(id);
        assertThat(jwtParser.getRoleFromJwt(jwt)).isEqualTo(authority);
    }

}
