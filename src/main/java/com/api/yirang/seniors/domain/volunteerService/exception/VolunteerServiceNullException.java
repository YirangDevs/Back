package com.api.yirang.seniors.domain.volunteerService.exception;

import com.api.yirang.common.exceptions.ApiException;
import com.api.yirang.common.exceptions.NullException;

import java.util.function.Supplier;

public class VolunteerServiceNullException extends NullException {

    public VolunteerServiceNullException() {
        super("VolunteerService Not Found");
    }
}
