package com.api.yirang.auth.presentation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
public class RegisterDto{

    private final Long userId;
    private final String username;
    private final String fileUrl;
    private final String sex;
    private final String email;

    @Builder
    public RegisterDto(Long userId, String username, String fileUrl,
                       String sex, String email) {
        this.userId = userId;
        this.username = username;
        this.fileUrl = fileUrl;
        this.sex = sex;
        this.email = email;
    }

    public RegisterDto() {
        this.userId = null;
        this.username = null;
        this.fileUrl = null;
        this.sex = null;
        this.email = null;
    }
}
