package com.api.yirang.auth.presentation.dto;


import com.api.yirang.common.support.type.Sex;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserInfoResponseDto {

    private final String username;
    private final String imageUrl;
    private final Sex sex;
    private final String email;

    public UserInfoResponseDto() {
        this.username = null;
        this.imageUrl = null;
        this.sex = null;
        this.email = null;
    }

    @Builder
    public UserInfoResponseDto(String username, String imageUrl, Sex sex, String email) {
        this.username = username;
        this.imageUrl = imageUrl;
        this.sex = sex;
        this.email = email;
    }
}
