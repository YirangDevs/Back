package com.api.yirang.auth.presentation.VO;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class SignInResponseVO {

    private final String yirangAccessToken;
    private final Boolean isNewbie;

    @Builder
    public SignInResponseVO(String yirangAccessToken, Boolean isNewbie) {
        this.yirangAccessToken = yirangAccessToken;
        this.isNewbie = isNewbie;
    }

    public SignInResponseVO() {
        this.yirangAccessToken = null;
        this.isNewbie = null;
    }
}
