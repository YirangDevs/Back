package com.api.yirang.img.exception;

import com.api.yirang.common.exceptions.DuplicatedException;

public class ImgTypeDuplicatedException extends DuplicatedException {

    public ImgTypeDuplicatedException() {
        super("ImgType is Duplicated");
    }
}
