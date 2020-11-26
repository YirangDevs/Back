package com.api.yirang.auth.application.basicService;

import com.api.yirang.auth.domain.kakaoToken.converter.KakaoTokenConverter;
import com.api.yirang.auth.domain.kakaoToken.dto.KakaoUserInfo;
import com.api.yirang.auth.domain.kakaoToken.dto.KakaoUserInfoDto;
import com.api.yirang.auth.repository.api.KakaoInfoAPI;
import com.api.yirang.auth.repository.api.KakaoTokenAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoTokenService {

    // Kakao API repo DI
    private final KakaoInfoAPI kakaoInfoAPI;
    private final KakaoTokenAPI kakaoTokenAPI;

    // check Validation
    public boolean isValidAccessToken(String kakaoAccessToken){
        System.out.println("[KakaoTokenService]: Token을 Validate합니다.");
        kakaoTokenAPI.isValidKakaoAccessToken(kakaoAccessToken);
        return true;
    }
    // get or Find
    public Long getUserIdByToken(String kakaoAccessToken){
        return kakaoTokenAPI.getUserId(kakaoAccessToken);
    }

    public KakaoUserInfo getUserInfoByToken(String kakaoAccessToken) {
        KakaoUserInfoDto kakaoUserInfoDto = kakaoInfoAPI.getUserInfo(kakaoAccessToken);
        return KakaoTokenConverter.fromKakaoUserInfoDtoToKakaoUserInfo(kakaoUserInfoDto);
    }

}
