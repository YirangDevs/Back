package com.api.yirang.email.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class EmailRequestDto {

    @NotNull
    @NotEmpty
    @Pattern(regexp = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$",
            message = "Email should be email type!")
    private final String email;

    public EmailRequestDto() {
        this.email = null;
    }
}
