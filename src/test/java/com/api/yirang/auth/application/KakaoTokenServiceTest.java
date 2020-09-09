package com.api.yirang.auth.application;

import com.api.yirang.auth.application.basicService.KakaoTokenService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KakaoTokenServiceTest {

    @Autowired
    KakaoTokenService kakaoTokenService;

    // Mock variables
    String kakaoAccessToken = "qAONiOQRU_CVHBNLQcDwjZhegCnDtxpi1wxs9Ao9dRoAAAF0cWr7hQ";
    long userId = 1468416139;
    String kakaoRefreshToken = "refreshToken Test";
    long kakaoExpiredTime = 13454;

    @Test
    public void 토큰_유효성_검사하기(){
        kakaoTokenService.isValidAccessToken(kakaoAccessToken);
    }

    @Test
    public void 유저아이디_가져오기(){
        Long kakaoUserId = kakaoTokenService.getUserIdByToken(kakaoAccessToken);
        assertThat(kakaoUserId).isEqualTo(userId);
    }


}
