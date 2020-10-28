package com.api.yirang.common.presentation;


import com.api.yirang.common.domain.region.exception.AlreadyExistedRegion;
import com.api.yirang.common.domain.region.exception.RegionNullException;
import com.api.yirang.common.exceptions.ApiException;
import com.api.yirang.common.exceptions.Dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RegionExceptionHandler {

    @ExceptionHandler(value = {RegionNullException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public final ErrorDto handleNullException(ApiException apiException){
        return apiException.buildErrorDto();
    }

    @ExceptionHandler(value = {AlreadyExistedRegion.class})
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public final ErrorDto handleAlreadyExistException(ApiException apiException){
        return apiException.buildErrorDto();
    }
}
