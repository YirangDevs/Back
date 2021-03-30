package com.api.yirang.notices.domain.activity.converter;

import com.api.yirang.common.support.time.TimeConverter;
import com.api.yirang.common.support.type.Region;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.notices.presentation.dto.ActivityOneResponseDto;
import com.api.yirang.notices.presentation.dto.ActivityResponseDto;
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

    public static final ActivityResponseDto ConvertActivityToDto(Activity activity){
        String dtovString = TimeConverter.LocalDateTimeToString(activity.getDtov());
        String dtodString = TimeConverter.LocalDateTimeToString(activity.getDtod());

        Long id = activity.getActivityId();

        Long nor = activity.getNor();
        Long noa = activity.getNoa();

        String[] dtovArr = dtovString.split(" ");
        String dov = dtovArr[0];
        String tov = dtovArr[1];

        String dod = dtodString.split(" ")[0];

        String region = activity.getRegion().getRegionName();

        return ActivityResponseDto.builder()
                .activityId(id)
                .nor(nor)
                .noa(noa)
                .dov(dov)
                .tov(tov)
                .dod(dod)
                .region(region)
                .build();

    }

    public static final ActivityOneResponseDto ConvertOneActivityToDto(Activity activity){
        String dtovString = TimeConverter.LocalDateTimeToString(activity.getDtov());
        String dtodString = TimeConverter.LocalDateTimeToString(activity.getDtod());

        Long id = activity.getActivityId();

        Long nor = activity.getNor();
        Long noa = activity.getNoa();

        String[] dtovArr = dtovString.split(" ");
        String dov = dtovArr[0];
        String tov = dtovArr[1];

        String[] dtodArr = dtodString.split(" ");
        String dod = dtodArr[0];
        String tod = dtodArr[1];

        String region = activity.getRegion().getRegionName();
        String content = activity.getContent();

        return ActivityOneResponseDto.builder()
                                  .activityId(id)
                                  .nor(nor)
                                  .noa(noa)
                                  .dov(dov)
                                  .tov(tov)
                                  .dod(dod)
                                  .tod(tod).region(region).content(content).build();
    }



}
