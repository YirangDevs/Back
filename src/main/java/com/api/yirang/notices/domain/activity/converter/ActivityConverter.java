package com.api.yirang.notices.domain.activity.converter;

import com.api.yirang.common.support.time.TimeConverter;
import com.api.yirang.common.support.type.Region;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.notices.presentation.dto.embeded.ActivityRegisterRequestDto;

import java.time.LocalDateTime;

public class ActivityConverter {

    public static final Activity ConvertFromDtoToModel(ActivityRegisterRequestDto activityRegisterRequestDto){

        // 시간 속성 친구들 다 빼서 Preprocessing
        String dov = activityRegisterRequestDto.getDov();
        String tov = activityRegisterRequestDto.getTov();
        String dod = activityRegisterRequestDto.getDod();
        Region region = activityRegisterRequestDto.getRegion();
        String endTimeOfDod = "23:59:59";

        LocalDateTime dtov = TimeConverter.StringToLocalDateTime(dov + " " + tov);
        LocalDateTime dtod = TimeConverter.StringToLocalDateTime(dod + " " + endTimeOfDod);

        return Activity.builder()
                       .nor(activityRegisterRequestDto.getNor())
                       .content(activityRegisterRequestDto.getContent())
                       .dtov(dtov)
                       .dtod(dtod)
                       .region(region)
                       .build();

    }
}
