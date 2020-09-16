package com.api.yirang.auth.presentation.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
public class SignInRequestDto {
    private final String accessToken;
    private final String refreshToken;
    private final Long refreshTokenExpiredTime;

    @Builder
    public SignInRequestDto(String accessToken, String refreshToken, Long refreshTokenExpiredTime) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiredTime = refreshTokenExpiredTime;
    }

    public SignInRequestDto() {
        this.accessToken = null;
        this.refreshToken = null;
        this.refreshTokenExpiredTime = null;
    }
}
