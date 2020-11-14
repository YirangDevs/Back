package com.api.yirang.common.exceptions;

import java.util.function.Supplier;

public class NullException extends ApiException implements Supplier<NullException> {

    public NullException(String errorName) {
        super("044", errorName);
    }

    @Override
    public NullException get() {
        return this;
    }
}
