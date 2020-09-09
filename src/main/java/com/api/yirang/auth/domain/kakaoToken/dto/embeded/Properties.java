package com.api.yirang.auth.domain.kakaoToken.dto.embeded;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class Properties {
    private String nickname;

    @SerializedName("profile_image")
    private String fileUrl;

}
