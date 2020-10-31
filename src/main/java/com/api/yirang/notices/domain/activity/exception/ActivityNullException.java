package com.api.yirang.notices.domain.activity.exception;

import com.api.yirang.common.exceptions.ApiException;

import java.util.function.Supplier;

public class ActivityNullException extends ApiException implements Supplier<ActivityNullException> {

    public ActivityNullException() {
        super("012", "Activity Not Found");
    }

    @Override
    public ActivityNullException get() {
        return this;
    }
}
