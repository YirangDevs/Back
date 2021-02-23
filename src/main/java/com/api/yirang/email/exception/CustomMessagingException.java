package com.api.yirang.email.exception;

import com.api.yirang.common.exceptions.UtilException;

public class CustomMessagingException extends UtilException {

    public CustomMessagingException() {
        super("There is some Error during Sending message");
    }
}
