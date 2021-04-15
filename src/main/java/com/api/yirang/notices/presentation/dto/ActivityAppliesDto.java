package com.api.yirang.notices.presentation.dto;

import com.api.yirang.notices.presentation.dto.embeded.ActivityApplyDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class ActivityAppliesDto {

    private final List<ActivityApplyDto> appliers;

    public ActivityAppliesDto() {
        this.appliers = null;
    }

    @Builder
    public ActivityAppliesDto(List<ActivityApplyDto> appliers) {
        this.appliers = appliers;
    }
}
