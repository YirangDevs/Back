package com.api.yirang.auth.presentation.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignInRequestDto {
    private String accessToken;
    private String refreshToken;
    private Long refreshTokenExpiredTime;

    @Builder
    public SignInRequestDto(String accessToken, String refreshToken, Long refreshTokenExpiredTime) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiredTime = refreshTokenExpiredTime;
    }
}
