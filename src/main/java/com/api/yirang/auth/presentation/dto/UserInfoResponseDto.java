package com.api.yirang.auth.presentation.dto;


import com.api.yirang.common.support.type.Region;
import com.api.yirang.common.support.type.Sex;
import com.api.yirang.email.util.Consent;
import lombok.*;

@Data
@Builder
@RequiredArgsConstructor
public class UserInfoResponseDto {

    private final String username;
    private final String realname;
    private final Sex sex;
    private final String imgUrl;
    private final Consent isReceivingEmail;

    private final String phone;
    private final String email;

    private final Region firstRegion;
    private final Region secondRegion;

    public UserInfoResponseDto() {
        this.username = null;
        this.realname = null;
        this.imgUrl = null;
        this.isReceivingEmail = null;
        this.firstRegion = null;
        this.secondRegion = null;
        this.sex = null;
        this.phone = null;
        this.email = null;
    }
}
