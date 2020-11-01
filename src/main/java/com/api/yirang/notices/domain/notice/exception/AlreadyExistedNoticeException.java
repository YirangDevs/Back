package com.api.yirang.notices.domain.notice.exception;

import com.api.yirang.common.exceptions.ApiException;

public class AlreadyExistedNoticeException extends ApiException {

    public AlreadyExistedNoticeException() {
        super("012", "Notice Already exist");
    }
}
