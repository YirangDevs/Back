package com.api.yirang.seniors.domain.volunteerService.exception;

import com.api.yirang.common.exceptions.ApiException;

import java.util.function.Supplier;

public class VolunteerServiceNullException extends ApiException implements Supplier<VolunteerServiceNullException> {

    public VolunteerServiceNullException() {
        super("012", "VolunteerService Not Found");
    }

    @Override
    public VolunteerServiceNullException get() {
        return this;
    }
}
