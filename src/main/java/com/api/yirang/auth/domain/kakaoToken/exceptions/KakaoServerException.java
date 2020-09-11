package com.api.yirang.auth.domain.kakaoToken.exceptions;

import com.api.yirang.common.exceptions.OtherServerException;

public class KakaoServerException extends OtherServerException {
    public KakaoServerException() {
        super("501", "Kakao Server Problem1");
    }
}
