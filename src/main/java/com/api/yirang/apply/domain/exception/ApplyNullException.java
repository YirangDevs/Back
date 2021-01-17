package com.api.yirang.apply.domain.exception;

import com.api.yirang.common.exceptions.NullException;

/**
 * Created by JeongminYoo on 2021/1/6
 * Work with Yirang
 * Email: likemin0142@gmail.com
 * Blog: http://Beewoom.github.io
 * Github: http://github.com/Biewoom
 */
public class ApplyNullException extends NullException {

    public ApplyNullException() {
        super("Apply Not Found");
    }
}
