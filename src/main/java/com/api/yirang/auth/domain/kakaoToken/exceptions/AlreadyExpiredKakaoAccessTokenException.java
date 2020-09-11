package com.api.yirang.auth.domain.kakaoToken.exceptions;

import com.api.yirang.common.exceptions.ApiException;

public class AlreadyExpiredKakaoAccessTokenException extends ApiException {

    public AlreadyExpiredKakaoAccessTokenException() {
        super("002", "KAT Expired");
    }
}
