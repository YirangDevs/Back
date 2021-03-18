package com.api.yirang.common.exceptions;

import java.util.function.Supplier;

public class DuplicatedException extends ApiException implements Supplier<DuplicatedException> {

    public DuplicatedException(String errorName) {
        super("088", errorName);
    }

    @Override
    public DuplicatedException get() {
        return this;
    }
}
