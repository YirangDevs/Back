package com.api.yirang.apply.domain.converter;

import com.api.yirang.apply.domain.model.Apply;
import com.api.yirang.auth.domain.user.model.Volunteer;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.seniors.support.custom.ServiceType;

/**
 * Created by JeongminYoo on 2020/12/30
 * Work with Yirang
 * Email: likemin0142@gmail.com
 * Blog: http://Beewoom.github.io
 * Github: http://github.com/Biewoom
 */
public class ApplyConverter {

    public static Apply makeApplyfromInfos(ServiceType serviceType, Volunteer volunteer,
                                           Activity activity){
        return Apply.builder()
                    .serviceType(serviceType)
                    .volunteer(volunteer).activity(activity)
                    .build();
    }
}
