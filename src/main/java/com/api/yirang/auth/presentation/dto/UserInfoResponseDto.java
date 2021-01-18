package com.api.yirang.auth.presentation.dto;


import com.api.yirang.common.support.type.Sex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@Builder
public class UserInfoResponseDto {

    private final String username;
    private final Sex sex;

    private final String phone;
    private final String email;

    public UserInfoResponseDto() {
        this.username = null;
        this.phone = null;
        this.sex = null;
        this.email = null;
    }
}
