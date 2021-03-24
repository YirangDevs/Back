package com.api.yirang.notices.presentation.dto;

import lombok.Data;

import java.util.Collection;

@Data
public class ActivitiesPagingResponseDto {

    private final Collection<ActivityResponseDto> activityResponseDtos;

    public ActivitiesPagingResponseDto() {
        this.activityResponseDtos = null;
    }
}
