package com.api.yirang.auth.presentation.dto.UserInfo;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@RequiredArgsConstructor
public class UsernameUpdateRequestDto {

    @NotNull
    @NotEmpty
    @Length(min = 1, max = 10)
    private final String username;


    public UsernameUpdateRequestDto() {
        this.username = null;
    }
}
