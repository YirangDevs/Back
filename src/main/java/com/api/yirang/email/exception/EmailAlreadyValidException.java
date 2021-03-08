package com.api.yirang.email.exception;

import com.api.yirang.common.exceptions.UtilException;

public class EmailAlreadyValidException extends UtilException {

    public EmailAlreadyValidException() {
        super("Email Is already Valid");
    }
}
