package com.api.yirang.auth.presentation.dto.UserInfo;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@RequiredArgsConstructor
public class RealnameUpdateRequestDto {

    @NotNull
    @Length(min = 1, max = 10)
    private final String realname;

    public RealnameUpdateRequestDto() {
        this.realname = null;
    }
}
