package com.api.yirang.auth.domain.user.converter;

import com.api.yirang.auth.domain.kakaoToken.dto.KakaoUserInfo;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.presentation.dto.UserInfoResponseDto;
import com.api.yirang.auth.support.type.Authority;

public class UserConverter {

    public static final UserInfoResponseDto toUserInfoResponseDto(User user){
        return UserInfoResponseDto.builder()
                                  .username(user.getUsername())
                                  .email(user.getEmail())
                                  .sex(user.getSex())
                                  .build();
    }

    public static final User fromKakaoUserInfo(Long userId, KakaoUserInfo kakaoUserInfo, Authority authority){
        return User.builder()
                   .userId(userId)
                   .username(kakaoUserInfo.getUsername())
                   .sex(kakaoUserInfo.getSex())
                   .email(kakaoUserInfo.getEmail())
                   .authority(authority)
                   .build();
    }

}
