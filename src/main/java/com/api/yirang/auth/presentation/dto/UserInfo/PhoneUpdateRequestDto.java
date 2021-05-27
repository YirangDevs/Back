package com.api.yirang.auth.presentation.dto.UserInfo;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.annotation.WillNotClose;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@RequiredArgsConstructor
public class PhoneUpdateRequestDto {

    @NotNull
    @Pattern(regexp = "[0-9]*$",
            message = "Phone should be numbers!")
    @Length(min = 1, max = 15)
    private final String phone;

    public PhoneUpdateRequestDto() {
        this.phone = null;
    }
}
