package com.api.yirang.auth.presentation.VO;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RefreshResponseVO {

    private String yirangAccessToken;

    @Builder
    public RefreshResponseVO(String yirangAccessToken) {
        this.yirangAccessToken = yirangAccessToken;
    }
}
