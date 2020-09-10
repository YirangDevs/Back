package com.api.yirang.auth.presentation.errorhandler;

import com.api.yirang.auth.domain.kakaoToken.exceptions.*;
import com.api.yirang.common.exceptions.ApiException;
import com.api.yirang.common.exceptions.Dto.ErrorDto;
import com.api.yirang.common.exceptions.OtherServerException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class KakaoErrorHandler {

    @ExceptionHandler(value = {AlreadyExpiredKakaoAccessTokenException.class, InvalidKakaoAccessTokenException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public final ErrorDto handleKakaoAccessToken(ApiException apiException){
        return apiException.buildErrorDto();
    }

    @ExceptionHandler(value = {KakaoServerException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final ErrorDto handleKakaoServer(OtherServerException otherServerException){
        return otherServerException.buildErrorDto();
    }

    @ExceptionHandler(value = {AlreadyExpiredKakaoRefreshTokenException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public final ErrorDto handleKakaoRefreshToken(ApiException apiException){
        return apiException.buildErrorDto();
    }

    @ExceptionHandler(value = {KakaoTokenNullException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public final ErrorDto handleKakaoTokenNull(ApiException apiException){
        return apiException.buildErrorDto();
    }

}
