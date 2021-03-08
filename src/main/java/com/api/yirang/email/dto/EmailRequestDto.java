package com.api.yirang.email.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class EmailRequestDto {
    private final String email;

    public EmailRequestDto() {
        this.email = null;
    }
}
