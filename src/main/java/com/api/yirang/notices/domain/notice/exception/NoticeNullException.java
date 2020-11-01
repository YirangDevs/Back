package com.api.yirang.notices.domain.notice.exception;

import com.api.yirang.common.exceptions.ApiException;

import java.util.function.Supplier;

public class NoticeNullException extends ApiException implements Supplier<NoticeNullException> {

    public NoticeNullException() {
        super("011", "Notice Not Found");
    }

    @Override
    public NoticeNullException get() {
        return this;
    }
}
