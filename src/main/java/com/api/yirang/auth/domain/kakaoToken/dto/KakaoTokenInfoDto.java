package com.api.yirang.auth.domain.kakaoToken.dto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoTokenInfoDto implements Serializable {
    private Long id;

    @SerializedName(value="expires_in")
    private Integer expiresIn;

    private Integer appId;
}
