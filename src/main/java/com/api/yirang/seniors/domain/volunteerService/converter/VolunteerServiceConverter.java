package com.api.yirang.seniors.domain.volunteerService.converter;

import com.api.yirang.common.support.time.TimeConverter;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.seniors.domain.senior.model.Senior;
import com.api.yirang.seniors.domain.volunteerService.model.VolunteerService;
import com.api.yirang.seniors.presentation.dto.response.SeniorResponseDto;
import com.api.yirang.seniors.support.custom.ServiceType;

public class VolunteerServiceConverter {

    public static final VolunteerService convertToModel(ServiceType serviceType, Long priority,
                                                        Activity activity, Senior senior){
        return VolunteerService.builder()
                               .serviceType(serviceType).priority(priority)
                                .activity(activity).senior(senior)
                               .build();
    }

    public static final SeniorResponseDto convertFromModelToSeniorResponseDto(VolunteerService volunteerService){
        // Activity 구하기
        Activity activity = volunteerService.getActivity();
        // Senior 구하기
        Senior senior = volunteerService.getSenior();

        // Date 구하기
        String dtovStr = TimeConverter.LocalDateTimeToString(activity.getDtov());
        String dov = dtovStr.split(" ")[0];

        return SeniorResponseDto.builder()
                                .id(volunteerService.getVolunteerServiceId())
                                .name(senior.getSeniorName()).sex(senior.getSex())
                                .region(senior.getRegion().getRegionName())
                                .address(senior.getAddress()).phone(senior.getPhone())
                                .type(volunteerService.getServiceType())
                                .date(dov).priority(volunteerService.getPriority())
                                .build();
    }


}
