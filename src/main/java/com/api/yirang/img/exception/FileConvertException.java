package com.api.yirang.img.exception;

import com.api.yirang.common.exceptions.ApiException;

public class FileConvertException extends ApiException {

    public FileConvertException() {
        super("443", "There are Error While ConvertingFile");
    }
}
