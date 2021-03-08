package com.api.yirang.email.exception;

import com.api.yirang.common.exceptions.NullException;

public class EmailNullException extends NullException {

    public EmailNullException(Long userId) {
        super(String.format("Message with UserId#{} is not exist", userId));
    }
}
