package com.api.yirang.apply.presentation.converter;

import com.api.yirang.apply.domain.model.Apply;
import com.api.yirang.apply.presentation.dto.ApplicantResponseDto;
import com.api.yirang.apply.presentation.dto.ApplyResponseDto;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.common.support.time.TimeConverter;
import com.api.yirang.common.support.type.Sex;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.seniors.support.custom.ServiceType;

import javax.validation.constraints.NotNull;

/**
 * Created by JeongminYoo on 2021/1/6
 * Work with Yirang
 * Email: likemin0142@gmail.com
 * Blog: http://Beewoom.github.io
 * Github: http://github.com/Biewoom
 */
public class ApplyDtoConverter {

    public static ApplicantResponseDto makeApplicantResponseDto(final ServiceType serviceType, final User user){
        // User에서 정보 가져오기
        final Sex sex = user.getSex();
        final String name = user.getUsername();
        final String email = user.getEmail();

        return ApplicantResponseDto.builder()
                                   .sex(sex).name(name)
                                   .serviceType(serviceType).email(email)
                                   .build();
    }

    public static ApplyResponseDto makeApplyResponseFromApply(final Apply apply){
        final Activity activity = apply.getActivity();

        return ApplyResponseDto.builder()
                               .applyId(apply.getApplyId()).dtoa(TimeConverter.LocalDateTimeToString(apply.getDtoa()))
                               .matchingState(apply.getMatchingState())
                               .dtov(TimeConverter.LocalDateTimeToString(activity.getDtov()))
                               .region(activity.getRegion())
                               .activityId(activity.getActivityId())
                               .serviceType(apply.getServiceType())
                               .build();
    }
}
