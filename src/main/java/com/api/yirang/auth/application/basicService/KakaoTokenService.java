package com.api.yirang.auth.application.basicService;

import com.api.yirang.auth.domain.kakaoToken.converter.KakaoTokenConverter;
import com.api.yirang.auth.domain.kakaoToken.dto.KakaoUserInfo;
import com.api.yirang.auth.domain.kakaoToken.dto.KakaoUserInfoDto;
import com.api.yirang.auth.domain.kakaoToken.exceptions.KakaoTokenNullException;
import com.api.yirang.auth.domain.kakaoToken.model.KakaoToken;
import com.api.yirang.auth.repository.api.KakaoInfoAPI;
import com.api.yirang.auth.repository.api.KakaoTokenAPI;
import com.api.yirang.auth.repository.persistence.h2.KakaoTokenDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class KakaoTokenService {

    // Kakao API repo DI
    private final KakaoInfoAPI kakaoInfoAPI;
    private final KakaoTokenAPI kakaoTokenAPI;

    // KakaoTokenDao(h2) DI
    private final KakaoTokenDao kakaoTokenDao;


    // check Validation
    public boolean isValidAccessToken(String kakaoAccessToken){
        kakaoTokenAPI.isValidKakaoAccessToken(kakaoAccessToken);
        return true;
    }
    public boolean isValidKakaoRefreshToken(Long userId) {
        // Checking 카카오 토큰을 찾습니다.
        System.out.println("checking KakaoToken for userID: " + userId);

        KakaoToken kakaoToken = findKakaoTokenByUserId(userId);
        return kakaoToken.getKakaoRefreshExpiredTime().isAfter(LocalDateTime.now());
    }

    // get or Find
    public Long getUserIdByToken(String kakaoAccessToken){
        return kakaoTokenAPI.getUserId(kakaoAccessToken);
    }

    public KakaoToken findKakaoTokenByUserId(Long userId){
        return kakaoTokenDao.findByUserId(userId).orElseThrow(KakaoTokenNullException::new);
    }

    public KakaoUserInfo getUserInfoByToken(String kakaoAccessToken) {
        KakaoUserInfoDto kakaoUserInfoDto = kakaoInfoAPI.getUserInfo(kakaoAccessToken);
        return KakaoTokenConverter.fromKakaoUserInfoDtoToKakaoUserInfo(kakaoUserInfoDto);
    }

    // save
    @Transactional
    public void saveKakaoToken(KakaoToken kakaoToken){
        // 정보가 없으면 추가, 아니면 지우고 다시 추가
        Long userId = kakaoToken.getUserId();
        if(kakaoTokenDao.existsByUserId(userId)){
            System.out.println("[kakaoTokenService] 기존에 userId가 등록이 되어있습니다!");
            // 삭제
            kakaoTokenDao.deleteByUserId(userId);
            System.out.println("[kakaoTokenService] 기존에 등록되어 있는 KakaoToken들을 지웠습니다!");
        }
        kakaoTokenDao.save(kakaoToken);
        System.out.println("[kakaoTokenService] kakoToken을 저장하겠습니다.!");
    }

}
