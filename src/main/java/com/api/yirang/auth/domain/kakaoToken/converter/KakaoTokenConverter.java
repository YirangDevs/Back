package com.api.yirang.auth.domain.kakaoToken.converter;

import com.api.yirang.auth.domain.kakaoToken.dto.KakaoUserInfo;
import com.api.yirang.auth.domain.kakaoToken.dto.KakaoUserInfoDto;

public class KakaoTokenConverter {

    public static final KakaoUserInfo fromKakaoUserInfoDtoToKakaoUserInfo(KakaoUserInfoDto kakaoUserInfoDto){

        String username = (kakaoUserInfoDto.getProperties().getNickname() == null)
                ? "unknown" : kakaoUserInfoDto.getProperties().getNickname();

        String fileUrl = ( kakaoUserInfoDto.getProperties().getFileUrl() == null )
                ? "unknown" : kakaoUserInfoDto.getProperties().getFileUrl();

        String sex = (kakaoUserInfoDto.getKakaoAccount().getGender() == null )
                ? "unknown" : kakaoUserInfoDto.getKakaoAccount().getGender();

        String email = (kakaoUserInfoDto.getKakaoAccount().getEmail() == null)
                ? "unknown" :kakaoUserInfoDto.getKakaoAccount().getEmail();

        return KakaoUserInfo.builder()
                            .username(username)
                            .fileUrl(fileUrl)
                            .sex(sex)
                            .email(email)
                            .build();
    }

}
