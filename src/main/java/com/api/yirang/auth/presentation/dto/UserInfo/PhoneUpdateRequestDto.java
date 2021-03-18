package com.api.yirang.auth.presentation.dto.UserInfo;


import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@RequiredArgsConstructor
public class PhoneUpdateRequestDto {

    @NotNull
    @Pattern(regexp = "[0-9]*$",
            message = "Phone should be numbers!")
    private final String phone;

    public PhoneUpdateRequestDto() {
        this.phone = null;
    }
}
