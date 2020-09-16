package com.api.yirang.auth.presentation.errorhandler;

import com.api.yirang.auth.domain.jwt.exceptions.InvalidJwtException;
import com.api.yirang.common.exceptions.ApiException;
import com.api.yirang.common.exceptions.Dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class JwtErrorHandler {

    @ExceptionHandler(value = {InvalidJwtException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public final ErrorDto handleInvalidJwtException(ApiException apiException){
        return apiException.buildErrorDto();
    }
}
