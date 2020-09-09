package com.api.yirang.auth.domain.kakaoToken.exceptions;

import com.api.yirang.common.exceptions.OtherServerException;

public class KakaoServerException extends OtherServerException {
    public KakaoServerException() {
        super("501", "카카오 서버에 문자가 있음");
    }
}
