package com.api.yirang.email.exception;

import com.api.yirang.common.exceptions.NullException;

public class EmailAddressNullException extends NullException {
    public EmailAddressNullException() {
        super("EmailAddress is Null or empty");
    }
}
