package com.api.yirang.notices.presentation.dto.embeded;

import com.api.yirang.common.support.type.Sex;
import lombok.Builder;
import lombok.Data;

@Data
public class ActivityApplyDto {

    private final Long userId;
    private final String realname;
    private final String email;
    private final Sex sex;
    private final String phone;
    private final String profileImg;

    @Builder
    public ActivityApplyDto(Long userId, String realname, String email, Sex sex, String phone, String profileImg) {
        this.userId = userId;
        this.realname = realname;
        this.email = email;
        this.sex = sex;
        this.phone = phone;
        this.profileImg = profileImg;
    }

    public ActivityApplyDto() {
        this.profileImg = null;
        this.userId = null;
        this.realname = null;
        this.email = null;
        this.sex = null;
        this.phone = null;
    }
}
