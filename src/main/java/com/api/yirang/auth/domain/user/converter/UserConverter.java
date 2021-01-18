package com.api.yirang.auth.domain.user.converter;

import com.api.yirang.auth.domain.kakaoToken.dto.KakaoUserInfo;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.presentation.dto.UserInfoResponseDto;
import com.api.yirang.auth.support.type.Authority;
import com.api.yirang.common.support.type.Sex;

public class UserConverter {

    public static UserInfoResponseDto toUserInfoResponseDto(User user){
        return UserInfoResponseDto.builder()
                                  .username(user.getUsername())
                                  .phone(user.getPhone())
                                  .email(user.getEmail())
                                  .sex(user.getSex())
                                  .build();
    }

    public static User fromKakaoUserInfo(Long userId, KakaoUserInfo kakaoUserInfo, Authority authority){
        return User.builder()
                   .userId(userId)
                   .username(kakaoUserInfo.getUsername())
                   .sex(Sex.deserialize(kakaoUserInfo.getSex()))
                   .email(kakaoUserInfo.getEmail())
                   .authority(authority)
                   .build();
    }

}
