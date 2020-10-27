package com.api.yirang.common.presentation.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RegionRequestDto {

    private final String regionName;

    public RegionRequestDto(){
        this.regionName = null;
    }

    @Builder
    public RegionRequestDto(String regionName) {
        this.regionName = regionName;
    }
}
