package com.api.yirang.auth.presentation.dto.UserInfo;


import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@RequiredArgsConstructor
public class UsernameUpdateRequestDto {

    @NotNull
    @NotEmpty
    private final String username;


    public UsernameUpdateRequestDto() {
        this.username = null;
    }
}
