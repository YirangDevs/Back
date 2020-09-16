package com.api.yirang.auth.domain.jwt.exceptions;

import com.api.yirang.common.exceptions.ApiException;

public class InvalidJwtException extends ApiException {
    public InvalidJwtException() {
        super("012", "Jwt Invalid");
    }
}
