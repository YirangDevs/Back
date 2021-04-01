package com.api.yirang.notices.presentation.dto;

import com.api.yirang.common.support.type.Region;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ActivityResponseDto {

    private final Long activityId;
    private final Long nor;
    private final Long noa;
    private final String dov;
    private final String tov;
    private final String dod;
    private final String region;
}
