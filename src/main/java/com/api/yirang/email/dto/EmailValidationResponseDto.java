package com.api.yirang.email.dto;

import com.api.yirang.email.util.Validation;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class EmailValidationResponseDto {

    private final Validation validation;

    public EmailValidationResponseDto() {
        this.validation = null;
    }
}
