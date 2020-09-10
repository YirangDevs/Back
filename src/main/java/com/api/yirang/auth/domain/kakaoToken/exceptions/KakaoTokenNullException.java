package com.api.yirang.auth.domain.kakaoToken.exceptions;

import com.api.yirang.common.exceptions.ApiException;

public class KakaoTokenNullException extends ApiException {
    public KakaoTokenNullException() {
        super("006", "KakaoToken Not Found");
    }
}
