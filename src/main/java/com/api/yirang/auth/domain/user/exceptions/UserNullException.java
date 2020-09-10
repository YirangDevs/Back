package com.api.yirang.auth.domain.user.exceptions;

import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.common.exceptions.ApiException;

import java.util.function.Supplier;

public class UserNullException extends ApiException implements Supplier<User> {
    // TO-DO
    public UserNullException() {
        super("005", "User Not Found");
    }

    @Override
    public User get() {
        return null;
    }
}
