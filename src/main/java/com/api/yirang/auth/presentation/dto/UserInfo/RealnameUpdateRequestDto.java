package com.api.yirang.auth.presentation.dto.UserInfo;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@RequiredArgsConstructor
public class RealnameUpdateRequestDto {

    @NotNull
    private final String realname;

    public RealnameUpdateRequestDto() {
        this.realname = null;
    }
}
