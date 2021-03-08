package com.api.yirang.email.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class EmailValidationRequestDto {

    private final String certificationNumbers;

    public EmailValidationRequestDto() {
        this.certificationNumbers = null;
    }
}
