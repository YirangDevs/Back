package com.api.yirang.apply.domain.exception;

import com.api.yirang.common.exceptions.UtilException;

public class InValidApplyException extends UtilException {

    public InValidApplyException() {
        super("This Apply attempt is not valid.");
    }
}
