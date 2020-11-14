package com.api.yirang.notices.domain.activity.exception;

import com.api.yirang.common.exceptions.AlreadyException;
import com.api.yirang.common.exceptions.ApiException;

public class AlreadyExistedActivityException extends AlreadyException {

    public AlreadyExistedActivityException() {
        super("Activity Already Existed");
    }
}
