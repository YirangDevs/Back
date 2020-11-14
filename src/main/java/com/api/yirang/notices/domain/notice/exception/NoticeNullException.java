package com.api.yirang.notices.domain.notice.exception;

import com.api.yirang.common.exceptions.ApiException;
import com.api.yirang.common.exceptions.NullException;

import java.util.function.Supplier;

public class NoticeNullException extends NullException {

    public NoticeNullException() {
        super("Notice Not Found");
    }

}
