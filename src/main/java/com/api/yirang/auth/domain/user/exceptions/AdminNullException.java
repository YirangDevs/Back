package com.api.yirang.auth.domain.user.exceptions;

import com.api.yirang.auth.domain.user.model.Admin;
import com.api.yirang.common.exceptions.ApiException;

import java.util.function.Supplier;

public class AdminNullException extends ApiException implements Supplier<AdminNullException> {

    public AdminNullException() {
        super("009", "Admin Not Found");
    }

    @Override
    public AdminNullException get() {
        return this;
    }
}
