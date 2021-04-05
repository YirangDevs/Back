package com.api.yirang.email.dto;


import com.api.yirang.auth.support.type.Authority;
import com.api.yirang.common.support.time.TimeConverter;
import com.api.yirang.common.support.type.Sex;
import com.api.yirang.email.util.MailContentConverter;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserWithdrawMailContent {

    private final String requestUsername;
    private final String name;
    private final String authority;
    private final String sex;
    private final String phoneNumber;
    private final String email;
    private final String dtow;

    @Builder
    public UserWithdrawMailContent(String requestUsername, String name, Authority authority, Sex sex,
                                   String phoneNumber, String email) {
        this.requestUsername = requestUsername;
        this.name = name;
        this.authority = MailContentConverter.authorityToMailContentString(authority);
        this.sex = MailContentConverter.sexToMailContentString(sex);
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.dtow = TimeConverter.LocalDateTimeToMailContentString(LocalDateTime.now());
    }
}
