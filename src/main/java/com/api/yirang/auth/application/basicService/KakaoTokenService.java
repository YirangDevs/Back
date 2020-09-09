package com.api.yirang.auth.application.basicService;

import com.api.yirang.auth.domain.kakaoToken.model.KakaoToken;
import com.api.yirang.auth.repository.api.KakaoInfoAPI;
import com.api.yirang.auth.repository.api.KakaoTokenAPI;
import com.api.yirang.auth.repository.persistence.h2.KakaoTokenDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class KakaoTokenService {

    // Kakao API repo DI
    private final KakaoInfoAPI kakaoInfoAPI;
    private final KakaoTokenAPI kakaoTokenAPI;

    // KakaoTokenDao(h2) DI
    private final KakaoTokenDao kakaoTokenDao;

    public void isValidAccessToken(String kakaoAccessToken){
        kakaoTokenAPI.isValidKakaoAccessToken(kakaoAccessToken);
    }

    public Long getUserIdByToken(String kakaoAccessToken){
        return kakaoTokenAPI.getUserId(kakaoAccessToken);
    }

    public void saveKakaoToken(KakaoToken kakaoToken){
        // TO-DO
    }

    public KakaoToken findKakaoTokenByUserId(Long userId){
        return kakaoTokenDao.findByUserId(userId);
    }

    public boolean isValidKakaoRefreshToken(Long userId) {
        KakaoToken kakaoToken = findKakaoTokenByUserId(userId);
        return kakaoToken.getKakaoRefreshExpiredTime().isAfter(LocalDateTime.now());
    }

}
