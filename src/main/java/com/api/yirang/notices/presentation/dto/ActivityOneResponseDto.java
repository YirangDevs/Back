package com.api.yirang.notices.presentation.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActivityOneResponseDto {
    private final Long activityId;
    private final Long nor;
    private final Long noa;
    private final String dov;
    private final String tov;
    private final String tod;
    private final String dod;
    private final String region;
    private final String content;
}
