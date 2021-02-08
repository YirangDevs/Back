package com.api.yirang.auth.presentation.dto;


import com.api.yirang.auth.support.type.Authority;
import com.api.yirang.common.support.type.Region;
import com.api.yirang.common.support.type.Sex;
import lombok.*;

import java.util.Collection;

/**
 * purpose: 유저 정보 + 권한 정보
 */

@Getter
@ToString
@AllArgsConstructor
@Builder
public class UserAuthResponseDto {

    private final Long userId;
    private final Authority authority;
    private final String userName;
    private final Sex sex;
    private final String phone;
    private final String email;
    private final Collection<Region> regions;

    public UserAuthResponseDto() {
        this.userId = null;
        this.authority = null;
        this.userName = null;
        this.sex = null;
        this.phone = null;
        this.email = null;
        this.regions = null;
    }
}
