package com.api.yirang.auth.domain.user.converter;

import com.api.yirang.auth.domain.kakaoToken.dto.KakaoUserInfo;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.presentation.dto.RegisterDto;
import com.api.yirang.auth.presentation.dto.UserInfoResponseDto;
import com.api.yirang.auth.support.type.Authority;

public class UserConverter {

    public static final UserInfoResponseDto toUserInfoResponseDto(User user){
        return UserInfoResponseDto.builder()
                                  .username(user.getUsername())
                                  .imageUrl(user.getFileUrl())
                                  .email(user.getEmail())
                                  .sex(user.getSex())
                                  .build();
    }

    public static final User fromRegisterDto(RegisterDto registerDto, Authority authority){
        return User.builder()
                   .userId(registerDto.getUserId())
                   .username(registerDto.getUsername())
                   .email(registerDto.getEmail())
                   .fileUrl(registerDto.getFileUrl())
                   .sex(registerDto.getSex())
                   .authority(authority)
                   .build();
    }

    public static final User fromKakaoUserInfo(KakaoUserInfo kakaoUserInfo, Authority authority){
        return User.builder()
                   .username(kakaoUserInfo.getUsername())
                   .fileUrl(kakaoUserInfo.getFileUrl())
                   .sex(kakaoUserInfo.getSex())
                   .email(kakaoUserInfo.getEmail())
                   .authority(Authority.ROLE_USER)
                   .build();
    }

    // OverLoading
    public static final User fromKakaoUserInfo(Long userId, KakaoUserInfo kakaoUserInfo, Authority authority){
        return User.builder()
                   .userId(userId)
                   .username(kakaoUserInfo.getUsername())
                   .fileUrl(kakaoUserInfo.getFileUrl())
                   .sex(kakaoUserInfo.getSex())
                   .email(kakaoUserInfo.getEmail())
                   .authority(Authority.ROLE_USER)
                   .build();
    }

}
