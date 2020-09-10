package com.api.yirang.auth.presentation.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
public class UserInfoResponseDto {

    private final String username;
    private final String imageUrl;
    private final String sex;
    private final String email;

    public UserInfoResponseDto() {
        this.username = null;
        this.imageUrl = null;
        this.sex = null;
        this.email = null;
    }

    @Builder
    public UserInfoResponseDto(String username, String imageUrl, String sex, String email) {
        this.username = username;
        this.imageUrl = imageUrl;
        this.sex = sex;
        this.email = email;
    }
}
