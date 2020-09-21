package com.api.yirang.auth.domain.jwt.exceptions;

import com.api.yirang.common.exceptions.ApiException;

public class JwtTokenMissingException extends ApiException {
    public JwtTokenMissingException() {
        super("015", "No JWT token found in request headers");
    }
}
