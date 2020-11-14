package com.api.yirang.auth.domain.user.exceptions;

import com.api.yirang.common.exceptions.NullException;

import java.util.function.Supplier;

public class UserNullException extends NullException {
    public UserNullException() {
        super("User Not Found");
    }

}
