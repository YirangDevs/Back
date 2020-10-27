package com.api.yirang.auth.domain.kakaoToken.exceptions;

import com.api.yirang.common.exceptions.ApiException;

import java.util.function.Supplier;

public class InvalidKakaoAccessTokenException extends ApiException implements Supplier<InvalidKakaoAccessTokenException> {

    public InvalidKakaoAccessTokenException() {
        super("001", "KAT Not Validated");
    }

    @Override
    public InvalidKakaoAccessTokenException get() {
        return this;
    }
}
