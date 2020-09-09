package com.api.yirang.auth.domain.kakaoToken.dto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class KakaoTokenInfoDto{
    private Long id;

    @SerializedName(value="expires_in")
    private Integer expiresIn;

    private Integer appId;

    @Builder
    public KakaoTokenInfoDto(Long id, Integer expiresIn, Integer appId) {
        this.id = id;
        this.expiresIn = expiresIn;
        this.appId = appId;
    }
}
