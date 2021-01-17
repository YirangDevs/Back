package com.api.yirang.apply.domain.exception;

import com.api.yirang.common.exceptions.ApiException;

/**
 * Created by JeongminYoo on 2021/1/17
 * Work with Yirang
 * Email: likemin0142@gmail.com
 * Blog: http://Beewoom.github.io
 * Github: http://github.com/Biewoom
 */
public class NoaNegativeException extends ApiException {

    public NoaNegativeException() {
        super("222", "number of Applicants Should Be Positive");
    }
}
