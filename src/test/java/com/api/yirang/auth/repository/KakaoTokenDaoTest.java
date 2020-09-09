package com.api.yirang.auth.repository;

import com.api.yirang.auth.domain.kakaoToken.model.KakaoToken;
import com.api.yirang.auth.repository.persistence.h2.KakaoTokenDao;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KakaoTokenDaoTest {

    @Autowired
    KakaoTokenDao kakaoTokenDao;

    @After
    public void cleanUp(){
        kakaoTokenDao.deleteAll();
    }

    @Test
    public void 카카오_토큰_저장_후_불러오기(){
        long userId = 123;
        String kakoAccessToken = "kakaoAccessToken";
        String kakaoRefreshToken = "kakaoRefreshToken";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime kakaoRefreshExpiredTime = LocalDateTime.now();

        kakaoTokenDao.save(
                KakaoToken.builder()
                          .userId(userId)
                          .kakaoAccessToken(kakoAccessToken)
                          .kakaoRefreshToken(kakaoRefreshToken)
                          .kakaoRefreshExpiredTime(kakaoRefreshExpiredTime)
                          .build()
        );

        KakaoToken kakaoToken = kakaoTokenDao.findByUserId(userId);

        // for dubugging
        System.out.println("KakaoToken: " + kakaoToken);

        assertThat(kakaoToken.getUserId()).isEqualTo(userId);
        assertThat(kakaoToken.getKakaoAccessToken()).isEqualTo(kakoAccessToken);
        assertThat(kakaoToken.getKakaoRefreshToken()).isEqualTo(kakaoRefreshToken);
        assertThat(kakaoToken.getKakaoRefreshExpiredTime().format(formatter)).isEqualTo(kakaoRefreshExpiredTime.format(formatter));
    }
}
