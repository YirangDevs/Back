package com.api.yirang.common.presentation;

import com.api.yirang.common.exceptions.*;
import com.api.yirang.common.exceptions.Dto.ErrorDto;
import com.api.yirang.email.exception.CustomMessagingException;
import com.api.yirang.email.exception.EmailAlreadyValidException;
import com.api.yirang.email.exception.EmailCertificationFailException;
import com.api.yirang.email.exception.EmailDuplicatedException;
import com.api.yirang.img.exception.ImgTypeInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WholeExceptionHandler{

    @ExceptionHandler(value = {NullException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public final ErrorDto handleNullException(ApiException apiException){
        return apiException.buildErrorDto();
    }

    @ExceptionHandler(value = {AlreadyException.class})
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public final ErrorDto handleAlreadyExistException(ApiException apiException){
        return apiException.buildErrorDto();
    }

    @ExceptionHandler(value = {DuplicatedException.class})
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public final ErrorDto handleDuplicatedException(ApiException apiException){
        return apiException.buildErrorDto();
    }

    @ExceptionHandler(value = {UtilException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ErrorDto handleUtilException(ApiException apiException){
        return apiException.buildErrorDto();
    }

}
