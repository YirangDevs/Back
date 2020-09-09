package com.api.yirang.auth.presentation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class RegisterDto{

    private Long userId;
    private String username;
    private String fileUrl;
    private String sex;
    private String email;

    @Builder
    public RegisterDto(Long userId, String username, String fileUrl,
                       String sex, String email) {
        this.userId = userId;
        this.username = username;
        this.fileUrl = fileUrl;
        this.sex = sex;
        this.email = email;
    }
}
