package com.api.yirang.common.exceptions;

import java.util.function.Supplier;

public class AlreadyException extends ApiException implements Supplier<AlreadyException> {

    public AlreadyException(String errorName) {
        super("099", errorName);
    }

    @Override
    public AlreadyException get() {
        return this;
    }
}
