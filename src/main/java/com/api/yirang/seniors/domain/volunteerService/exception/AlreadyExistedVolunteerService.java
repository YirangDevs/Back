package com.api.yirang.seniors.domain.volunteerService.exception;

import com.api.yirang.common.exceptions.AlreadyException;

/**
 * Created by JeongminYoo on 2020/11/28
 * Work with Yirang
 * Email: likemin0142@gmail.com
 * Blog: http://Beewoom.github.io
 * Github: http://github.com/Biewoom
 */
public class AlreadyExistedVolunteerService extends AlreadyException {
    public AlreadyExistedVolunteerService() {
        super("VolunteerService Already Exist");
    }
}
