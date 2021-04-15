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

    @Builder
    public ActivityApplyDto(Long userId, String realname, String email, Sex sex, String phone) {
        this.userId = userId;
        this.realname = realname;
        this.email = email;
        this.sex = sex;
        this.phone = phone;
    }

    public ActivityApplyDto() {
        this.userId = null;
        this.realname = null;
        this.email = null;
        this.sex = null;
        this.phone = null;
    }
}
