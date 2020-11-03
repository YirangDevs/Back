package com.api.yirang.notices.domain.notice.exception;

import com.api.yirang.common.exceptions.ApiException;

public class LastExistedNotice extends ApiException {

    public LastExistedNotice() {
        super("99", "It is last Notice");
    }
}
