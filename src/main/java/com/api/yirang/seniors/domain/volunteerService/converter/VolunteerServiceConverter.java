package com.api.yirang.seniors.domain.volunteerService.converter;

import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.seniors.domain.senior.model.Senior;
import com.api.yirang.seniors.domain.volunteerService.model.VolunteerService;
import com.api.yirang.seniors.support.custom.ServiceType;

public class VolunteerServiceConverter {

    public static final VolunteerService convertToModel(ServiceType serviceType, Long priority,
                                                        Activity activity, Senior senior){
        return VolunteerService.builder()
                               .serviceType(serviceType).priority(priority)
                                .activity(activity).senior(senior)
                               .build();
    }
}
