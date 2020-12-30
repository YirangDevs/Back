package com.api.yirang.auth.domain.user.exceptions;

import com.api.yirang.common.exceptions.NullException;

/**
 * Created by JeongminYoo on 2020/12/30
 * Work with Yirang
 * Email: likemin0142@gmail.com
 * Blog: http://Beewoom.github.io
 * Github: http://github.com/Biewoom
 */
public class VolunteerNullException extends NullException {

    public VolunteerNullException() {
        super("Volunteer Not Found");
    }
}
