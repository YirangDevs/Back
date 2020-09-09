package com.api.yirang.auth.domain.kakaoToken.dto;


import com.google.gson.annotations.SerializedName;
import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class KakaoUserInfo{

    @NotNull
    private String username;

    @Nullable
    private String fileUrl;

    @Nullable
    private String sex;

    @Nullable
    private String email;

    @Builder
    public KakaoUserInfo(String username, String fileUrl, String sex, String email) {
        this.username = username;
        this.fileUrl = fileUrl;
        this.sex = sex;
        this.email = email;
    }
}
