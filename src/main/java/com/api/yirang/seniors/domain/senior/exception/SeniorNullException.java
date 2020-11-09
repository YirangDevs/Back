package com.api.yirang.seniors.domain.senior.exception;

import com.api.yirang.common.exceptions.ApiException;

import java.util.function.Supplier;

public class SeniorNullException extends ApiException implements Supplier<SeniorNullException> {


    public SeniorNullException() {
        super("012", "Senior Not Found");
    }

    @Override
    public SeniorNullException get() {
        return this;
    }
}
