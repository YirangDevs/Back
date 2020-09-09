package com.api.yirang.common.exceptions;

import com.api.yirang.common.exceptions.Dto.ErrorDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ApiException extends RuntimeException {
    private final String errorCode;
    private final String errorName;

    public ErrorDto buildErrorDto(){
        return ErrorDto.builder()
                       .errorCode(errorCode)
                       .errorName(errorName)
                       .build();
    }
}
