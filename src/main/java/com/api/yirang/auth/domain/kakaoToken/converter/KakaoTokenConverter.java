package com.api.yirang.auth.domain.kakaoToken.converter;

import com.api.yirang.auth.domain.kakaoToken.dto.KakaoUserInfo;
import com.api.yirang.auth.domain.kakaoToken.dto.KakaoUserInfoDto;
import com.api.yirang.auth.domain.kakaoToken.model.KakaoToken;
import com.api.yirang.auth.presentation.dto.SignInRequestDto;
import com.api.yirang.common.support.time.MyLocalTime;
import com.api.yirang.common.support.time.TimeConverter;

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

    public static final KakaoToken fromSignInRequestDto(SignInRequestDto signInRequestDto){
        return KakaoToken.builder()
                         .kakaoAccessToken(signInRequestDto.getAccessToken())
                         .kakaoRefreshToken(signInRequestDto.getRefreshToken())
                         .kakaoRefreshExpiredTime(
                                 TimeConverter.StringToLocalDateTime(
                                         MyLocalTime.makeExpiredTimeString(signInRequestDto.getRefreshTokenExpiredTime() )
                                 )
                         )
                         .build();
    }
    public static final KakaoToken fromSignInRequestDto(Long userId, SignInRequestDto signInRequestDto){
        return KakaoToken.builder()
                         .userId(userId)
                         .kakaoAccessToken(signInRequestDto.getAccessToken())
                         .kakaoRefreshToken(signInRequestDto.getRefreshToken())
                         .kakaoRefreshExpiredTime(
                                 TimeConverter.StringToLocalDateTime(
                                         MyLocalTime.makeExpiredTimeString(signInRequestDto.getRefreshTokenExpiredTime() )
                                 )
                         )
                         .build();
    }

}
