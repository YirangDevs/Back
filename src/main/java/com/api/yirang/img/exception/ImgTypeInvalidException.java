package com.api.yirang.img.exception;

import com.api.yirang.common.exceptions.UtilException;

public class ImgTypeInvalidException extends UtilException {

    public ImgTypeInvalidException() {
        super("ImgType is Not valid for executing request");
    }
}
