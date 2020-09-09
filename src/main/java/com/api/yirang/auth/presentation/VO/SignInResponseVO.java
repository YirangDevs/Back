package com.api.yirang.auth.presentation.VO;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignInResponseVO {

    private String YirangAccessToken;

    @Builder
    public SignInResponseVO(String yirangAccessToken) {
        YirangAccessToken = yirangAccessToken;
    }
}
