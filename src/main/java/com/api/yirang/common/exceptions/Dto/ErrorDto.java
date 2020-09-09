package com.api.yirang.common.exceptions.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorDto {

    private String errorCode;
    private String errorName;

    @Builder
    public ErrorDto(String errorCode, String errorName) {
        this.errorCode = errorCode;
        this.errorName = errorName;
    }
}
