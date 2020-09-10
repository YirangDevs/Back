package com.api.yirang.auth.domain.kakaoToken.exceptions;

import com.api.yirang.common.exceptions.ApiException;

public class AlreadyExpiredKakaoRefreshTokenException extends ApiException {

    public AlreadyExpiredKakaoRefreshTokenException() {
        super("004", "KRT Expired");
    }
}
