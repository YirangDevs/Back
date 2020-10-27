package com.api.yirang.auth.domain.kakaoToken.exceptions;

import com.api.yirang.common.exceptions.ApiException;

import java.util.function.Supplier;

public class AlreadyExpiredKakaoAccessTokenException extends ApiException implements Supplier<AlreadyExpiredKakaoAccessTokenException> {

    public AlreadyExpiredKakaoAccessTokenException() {
        super("002", "KAT Expired");
    }

    @Override
    public AlreadyExpiredKakaoAccessTokenException get() {
        return new AlreadyExpiredKakaoAccessTokenException();
    }
}
