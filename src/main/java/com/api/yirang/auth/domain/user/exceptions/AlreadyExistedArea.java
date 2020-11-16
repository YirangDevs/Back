package com.api.yirang.auth.domain.user.exceptions;

import com.api.yirang.common.exceptions.AlreadyException;

public class AlreadyExistedArea extends AlreadyException {

    public AlreadyExistedArea() {
        super("This region is Already exist");
    }
}
