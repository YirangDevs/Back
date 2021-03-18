package com.api.yirang.auth.domain.user.converter;

import com.api.yirang.auth.domain.kakaoToken.dto.KakaoUserInfo;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.presentation.dto.UserAuthResponseDto;
import com.api.yirang.auth.presentation.dto.UserInfoResponseDto;
import com.api.yirang.auth.support.type.Authority;
import com.api.yirang.common.support.type.Region;
import com.api.yirang.common.support.type.Sex;
import com.api.yirang.email.util.Consent;

import java.util.Collection;

public class UserConverter {

    public static UserInfoResponseDto toUserInfoResponseDto(User user, String imgUrl, Consent notifiable){
        return UserInfoResponseDto.builder()
                                  .username(user.getUsername()). realname(user.getRealname())
                                  .phone(user.getPhone())
                                  .email(user.getEmail())
                                  .sex(user.getSex()).isReceivingEmail(notifiable)
                                  .imgUrl(imgUrl)
                                  .firstRegion(user.getFirstRegion()).secondRegion(user.getSecondRegion())
                                  .build();
    }

    public static UserAuthResponseDto toUserAuthResponseDto(User user, Collection<Region> regions){
        return UserAuthResponseDto.builder()
                                  .userId(user.getUserId()).authority(user.getAuthority())
                                  .userName(user.getUsername()).sex(user.getSex())
                                  .phone(user.getPhone()).email(user.getEmail())
                                  .regions(regions)
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
