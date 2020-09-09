package com.api.yirang.auth.application.basicService;

import com.api.yirang.auth.domain.kakaoToken.dto.KakaoUserInfo;
import com.api.yirang.auth.domain.kakaoToken.dto.KakaoUserInfoDto;
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
        kakaoTokenDao.save(kakaoToken);
    }

    public KakaoToken findKakaoTokenByUserId(Long userId){
        return kakaoTokenDao.findByUserId(userId);
    }

    public boolean isValidKakaoRefreshToken(Long userId) {
        KakaoToken kakaoToken = findKakaoTokenByUserId(userId);
        return kakaoToken.getKakaoRefreshExpiredTime().isAfter(LocalDateTime.now());
    }

    public KakaoUserInfo getUserInfoByToken(String kakaoAccessToken) {
        KakaoUserInfoDto kakaoUserInfoDto = kakaoInfoAPI.getUserInfo(kakaoAccessToken);
        String username = (kakaoUserInfoDto.getProperties().getNickname() == null)
                            ? "Unknown" : kakaoUserInfoDto.getProperties().getNickname();

        String fileUrl = ( kakaoUserInfoDto.getProperties().getFileUrl() == null )
                            ? "Unknown" : kakaoUserInfoDto.getProperties().getFileUrl();

        String sex = (kakaoUserInfoDto.getKakaoAccount().getGender() == null )
                            ? "Unknown" : kakaoUserInfoDto.getKakaoAccount().getGender();

        String email = (kakaoUserInfoDto.getKakaoAccount().getEmail() == null)
                        ? "Unknown" :kakaoUserInfoDto.getKakaoAccount().getEmail();

        return KakaoUserInfo.builder()
                            .username(username)
                            .fileUrl(fileUrl)
                            .sex(sex)
                            .email(email)
                            .build();
    }


}
