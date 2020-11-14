package com.api.yirang.notices.domain.activity.exception;

import com.api.yirang.common.exceptions.ApiException;
import com.api.yirang.common.exceptions.NullException;

import java.util.function.Supplier;

public class ActivityNullException extends NullException {

    public ActivityNullException() {
        super("Activity Not Found");
    }

}
