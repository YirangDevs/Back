package com.api.yirang.email.exception;

import com.api.yirang.common.exceptions.DuplicatedException;
import com.api.yirang.common.exceptions.UtilException;

public class EmailDuplicatedException extends DuplicatedException {
    public EmailDuplicatedException() {
        super("This is Email is duplicated");
    }
}
