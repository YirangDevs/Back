package com.api.yirang.notices.presentation.errorhandler;

import com.api.yirang.common.exceptions.ApiException;
import com.api.yirang.common.exceptions.Dto.ErrorDto;
import com.api.yirang.notices.domain.activity.exception.ActivityNullException;
import com.api.yirang.notices.domain.notice.exception.LastExistedNotice;
import com.api.yirang.notices.domain.notice.exception.AlreadyExistedNoticeException;
import com.api.yirang.notices.domain.notice.exception.NoticeNullException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NoticeExceptionHandler {
    
    @ExceptionHandler(value = {LastExistedNotice.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public final ErrorDto handleAlreadyExistException(ApiException apiException){
        return apiException.buildErrorDto();
    }
}
