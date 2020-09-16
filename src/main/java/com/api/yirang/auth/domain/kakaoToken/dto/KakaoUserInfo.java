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
public class KakaoUserInfo{

    @NotNull
    private final String username;

    @Nullable
    private final String fileUrl;

    @Nullable
    private final String sex;

    @Nullable
    private final String email;

    @Builder
    public KakaoUserInfo(String username, String fileUrl, String sex, String email) {
        this.username = username;
        this.fileUrl = fileUrl;
        this.sex = sex;
        this.email = email;
    }

    public KakaoUserInfo() {
        this.username = null;
        this.fileUrl = null;
        this.sex = null;
        this.email = null;
    }
}
