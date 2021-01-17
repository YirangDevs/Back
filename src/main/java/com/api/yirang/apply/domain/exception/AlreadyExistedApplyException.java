package com.api.yirang.apply.domain.exception;

import com.api.yirang.common.exceptions.AlreadyException;

/**
 * Created by JeongminYoo on 2021/1/7
 * Work with Yirang
 * Email: likemin0142@gmail.com
 * Blog: http://Beewoom.github.io
 * Github: http://github.com/Biewoom
 */
public class AlreadyExistedApplyException extends AlreadyException {
    public AlreadyExistedApplyException() {
        super("Apply Already Exists");
    }
}
