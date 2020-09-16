package com.api.yirang.auth.domain.kakaoToken.dto.embeded;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
public class Properties {
    private final String nickname;

    @SerializedName("profile_image")
    private final String fileUrl;

    public Properties() {
        this.nickname = null;
        this.fileUrl = null;
    }
}
