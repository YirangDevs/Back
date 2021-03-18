package com.api.yirang.img.exception;

import com.api.yirang.common.exceptions.NullException;

public class ImageNullException extends NullException {

    public ImageNullException() {
        super("Image table does not exist");
    }
}
