package com.api.yirang.auth.domain.kakaoToken.exceptions;

import com.api.yirang.common.exceptions.ApiException;

public class InvalidKakaoAccessTokenException extends ApiException {

    public InvalidKakaoAccessTokenException() {
        super("001", "KAT가 검증되지 않음");
    }
}
