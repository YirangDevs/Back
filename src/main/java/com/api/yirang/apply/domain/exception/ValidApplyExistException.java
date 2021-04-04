package com.api.yirang.apply.domain.exception;

import com.api.yirang.common.exceptions.UtilException;

public class ValidApplyExistException extends UtilException {
    public ValidApplyExistException() {
        super("This User Has one or more than one apply alive");
    }
}
