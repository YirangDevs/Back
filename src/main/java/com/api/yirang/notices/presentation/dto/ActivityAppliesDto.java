package com.api.yirang.notices.presentation.dto;

import com.api.yirang.notices.presentation.dto.embeded.ActivityApplyDto;
import lombok.Data;

@Data
public class ActivityAppliesDto {

    private final ActivityApplyDto appliers;

    public ActivityAppliesDto() {
        this.appliers = null;
    }
}
