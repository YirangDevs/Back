package com.api.yirang.auth.domain.kakaoToken.dto;

import com.google.gson.annotations.SerializedName;
import lombok.*;

@Getter
@ToString
public class KakaoTokenInfoDto{
    private final Long id;

    @SerializedName(value="expires_in")
    private final Integer expiresIn;

    private final Integer appId;

    @Builder
    public KakaoTokenInfoDto(Long id, Integer expiresIn, Integer appId) {
        this.id = id;
        this.expiresIn = expiresIn;
        this.appId = appId;
    }

    public KakaoTokenInfoDto() {
        this.id = null;
        this.expiresIn = null;
        this.appId = null;
    }
}
