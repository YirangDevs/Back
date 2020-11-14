package com.api.yirang.seniors.domain.senior.exception;

import com.api.yirang.common.exceptions.ApiException;
import com.api.yirang.common.exceptions.NullException;

import java.util.function.Supplier;

public class SeniorNullException extends NullException {

    public SeniorNullException() {
        super("Senior Not Found");
    }
}
