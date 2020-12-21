package com.api.yirang.notices.domain.notice.exception;

import com.api.yirang.common.exceptions.AlreadyException;

public class AlreadyExistedNoticeException extends AlreadyException {

    public AlreadyExistedNoticeException() {
        super("Notice Already Exist");
    }
}
