package com.api.yirang.auth.domain.user.exceptions;

import com.api.yirang.common.exceptions.ApiException;

public class AlreadyExistedAdmin extends ApiException {

    public AlreadyExistedAdmin() {
        super("020", "Admin Already exist");
    }
}
