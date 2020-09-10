package com.api.yirang.auth.presentation.VO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RefreshYatResponseVO {

    private String YirangAccessToken;

    @Builder
    public RefreshYatResponseVO(String yirangAccessToken) {
        YirangAccessToken = yirangAccessToken;
    }
}
