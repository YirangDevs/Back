package com.api.yirang.seniors.presentation.errorhandler;

import com.api.yirang.common.exceptions.ApiException;
import com.api.yirang.common.exceptions.Dto.ErrorDto;
import com.api.yirang.notices.domain.activity.exception.ActivityNullException;
import com.api.yirang.notices.domain.notice.exception.AlreadyExistedNoticeException;
import com.api.yirang.notices.domain.notice.exception.LastExistedNotice;
import com.api.yirang.notices.domain.notice.exception.NoticeNullException;
import com.api.yirang.seniors.domain.senior.exception.SeniorNullException;
import com.api.yirang.seniors.domain.volunteerService.exception.VolunteerServiceNullException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SeniorExceptionHandler {

    @ExceptionHandler(value = {SeniorNullException.class, VolunteerServiceNullException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public final ErrorDto handleNullException(ApiException apiException){
        return apiException.buildErrorDto();
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public final ErrorDto handleAlreadyExistException(ApiException apiException){
        return apiException.buildErrorDto();
    }
}
