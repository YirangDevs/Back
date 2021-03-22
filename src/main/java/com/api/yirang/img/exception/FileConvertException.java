package com.api.yirang.img.exception;

import com.api.yirang.common.exceptions.ApiException;
import com.api.yirang.common.exceptions.UtilException;

public class FileConvertException extends UtilException {


    public FileConvertException() {
        super("There is Error While Convert MultipartFile to File");
    }
}
