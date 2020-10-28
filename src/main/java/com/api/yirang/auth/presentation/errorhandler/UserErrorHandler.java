package com.api.yirang.auth.presentation.errorhandler;

import com.api.yirang.auth.domain.user.exceptions.AdminNullException;
import com.api.yirang.auth.domain.user.exceptions.AlreadyExistedAdmin;
import com.api.yirang.auth.domain.user.exceptions.UserNullException;
import com.api.yirang.common.exceptions.ApiException;
import com.api.yirang.common.exceptions.Dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserErrorHandler {

    @ExceptionHandler(value={AdminNullException.class, UserNullException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public final ErrorDto handleUserNull(ApiException apiException){
        return apiException.buildErrorDto();
    }

    @ExceptionHandler(value = {AlreadyExistedAdmin.class})
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public final ErrorDto handleExistedUser(ApiException apiException){
        return apiException.buildErrorDto();
    }

}
