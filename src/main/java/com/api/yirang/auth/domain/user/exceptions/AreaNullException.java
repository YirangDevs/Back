package com.api.yirang.auth.domain.user.exceptions;

import com.api.yirang.common.exceptions.NullException;

public class AreaNullException extends NullException {
    public AreaNullException() {
        super("This Area Not Found");
    }
}
