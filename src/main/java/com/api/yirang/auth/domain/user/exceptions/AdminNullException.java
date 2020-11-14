package com.api.yirang.auth.domain.user.exceptions;

import com.api.yirang.auth.domain.user.model.Admin;
import com.api.yirang.common.exceptions.ApiException;
import com.api.yirang.common.exceptions.NullException;

import java.util.function.Supplier;

public class AdminNullException extends NullException {

    public AdminNullException() {
        super("Admin Not Found");
    }

}
