package com.api.yirang.auth.presentation.dto.UserInfo;

import com.api.yirang.common.support.type.Sex;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@RequiredArgsConstructor
public class SexUpdateRequestDto {

    @NotNull
    private final Sex sex;

    public SexUpdateRequestDto() {
        this.sex = null;
    }
}
