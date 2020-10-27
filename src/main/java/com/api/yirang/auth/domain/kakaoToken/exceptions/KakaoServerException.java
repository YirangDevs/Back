package com.api.yirang.auth.domain.kakaoToken.exceptions;

import com.api.yirang.common.exceptions.OtherServerException;

import java.util.function.Supplier;

public class KakaoServerException extends OtherServerException implements Supplier<KakaoServerException> {
    public KakaoServerException() {
        super("501", "Kakao Server Problem1");
    }

    @Override
    public KakaoServerException get() {
        return this;
    }


}
