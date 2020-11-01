package com.api.yirang.notices.domain.activity.exception;

import com.api.yirang.common.exceptions.ApiException;

public class AlreadyExistedActivityException extends ApiException {

    public AlreadyExistedActivityException() {
        super("012", "Activity Already Existed");
    }
}
