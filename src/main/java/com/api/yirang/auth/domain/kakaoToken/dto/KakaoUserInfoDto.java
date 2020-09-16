package com.api.yirang.auth.domain.kakaoToken.dto;

import com.api.yirang.auth.domain.kakaoToken.dto.embeded.KakaoAccount;
import com.api.yirang.auth.domain.kakaoToken.dto.embeded.Properties;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@ToString
public class KakaoUserInfoDto {

    private final Long id;

    @SerializedName("connected_at")
    private final String connectAt;

    private final Properties properties;

    @SerializedName("kakao_account")
    private final KakaoAccount kakaoAccount;

    public KakaoUserInfoDto() {
        this.id = null;
        this.connectAt = null;
        this.properties = null;
        this.kakaoAccount = null;
    }
}
