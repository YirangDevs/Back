package com.api.yirang.img.exception;

import com.api.yirang.common.exceptions.DuplicatedException;

public class ImgDuplicatedException extends DuplicatedException {

    public ImgDuplicatedException() {
        super("Img is Duplicated");
    }
}
