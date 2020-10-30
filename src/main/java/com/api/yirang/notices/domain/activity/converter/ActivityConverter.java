package com.api.yirang.notices.domain.activity.converter;

import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.notices.presentation.dto.embeded.ActivityRegisterRequestDto;

public class ActivityConverter {

    public static final Activity ConvertFromDtoToModel(ActivityRegisterRequestDto activityRegisterRequestDto){
        String dov = activityRegisterRequestDto.getDov();
        String tov = activityRegisterRequestDto.getTov();
        String = activityRegisterRequestDto.


        return Activity.builder()
                       .content(activityRegisterRequestDto.getContent())
                       .dov()
                       .region(activityRegisterRequestDto.getRegion())
                       .nor(activityRegisterRequestDto.getNor())
                       .build();

    }
}
