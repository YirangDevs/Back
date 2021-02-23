package com.api.yirang.common.exceptions;

public class UtilException extends ApiException{

    public UtilException(String errorName) {
        super("055", errorName);
    }
}
