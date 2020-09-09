package com.api.yirang.auth.domain.kakaoToken.dto;

import com.api.yirang.auth.domain.kakaoToken.dto.embeded.KakaoAccount;
import com.api.yirang.auth.domain.kakaoToken.dto.embeded.Properties;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@ToString
@NoArgsConstructor
public class KakaoUserInfoDto {

    private Long id;

    @SerializedName("connected_at")
    private String connectAt;

    private Properties properties;

    @SerializedName("kakao_account")
    private KakaoAccount kakaoAccount;
}
