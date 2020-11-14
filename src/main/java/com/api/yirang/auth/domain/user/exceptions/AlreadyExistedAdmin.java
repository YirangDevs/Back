package com.api.yirang.auth.domain.user.exceptions;

import com.api.yirang.common.exceptions.AlreadyException;
import com.api.yirang.common.exceptions.ApiException;

public class AlreadyExistedAdmin extends AlreadyException {

    public AlreadyExistedAdmin() {
        super("Admin Already exist");
    }
}
