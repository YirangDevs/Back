package com.api.yirang.notices.presentation.errorhandler;

import com.api.yirang.common.domain.region.exception.AlreadyExistedRegion;
import com.api.yirang.common.domain.region.exception.DistributionRegionNullException;
import com.api.yirang.common.domain.region.exception.RegionNullException;
import com.api.yirang.common.exceptions.ApiException;
import com.api.yirang.common.exceptions.Dto.ErrorDto;
import com.api.yirang.notices.domain.activity.exception.ActivityNullException;
import com.api.yirang.notices.domain.notice.exception.AlreadyExistedNoticeException;
import com.api.yirang.notices.domain.notice.exception.NoticeNullException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NoticeExceptionHandler {

    @ExceptionHandler(value = {NoticeNullException.class, ActivityNullException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public final ErrorDto handleNullException(ApiException apiException){
        return apiException.buildErrorDto();
    }

    @ExceptionHandler(value = {AlreadyExistedNoticeException.class})
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public final ErrorDto handleAlreadyExistException(ApiException apiException){
        return apiException.buildErrorDto();
    }
}
