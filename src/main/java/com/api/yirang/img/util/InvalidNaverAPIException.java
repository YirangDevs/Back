package com.api.yirang.img.util;

import com.api.yirang.common.exceptions.ApiException;

import java.util.function.Supplier;

public class InvalidNaverAPIException extends ApiException implements Supplier<InvalidNaverAPIException> {

    public InvalidNaverAPIException() {
        super("001", "NAVER API EXCEPTION");
    }

    @Override
    public InvalidNaverAPIException get() {
        return this;
    }
}
