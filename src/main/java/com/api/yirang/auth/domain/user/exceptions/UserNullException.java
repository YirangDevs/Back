package com.api.yirang.auth.domain.user.exceptions;

import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.common.exceptions.ApiException;

import java.util.function.Supplier;

public class UserNullException extends ApiException implements Supplier<User> {
    // TO-DO
    public UserNullException() {
        super("010", "User를 찾을 수 없음");
    }

    @Override
    public User get() {
        return null;
    }
}
