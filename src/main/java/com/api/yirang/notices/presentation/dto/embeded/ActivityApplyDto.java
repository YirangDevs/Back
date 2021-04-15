package com.api.yirang.notices.presentation.dto.embeded;

import com.api.yirang.common.support.type.Sex;
import lombok.Data;

@Data
public class ActivityApplyDto {

    private final Long userId;
    private final String realname;
    private final String email;
    private final Sex sex;
    private final String phone;



}
