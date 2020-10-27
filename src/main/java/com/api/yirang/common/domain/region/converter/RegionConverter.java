package com.api.yirang.common.domain.region.converter;

import com.api.yirang.common.domain.region.model.Region;
import com.api.yirang.common.presentation.dto.RegionRequestDto;

public class RegionConverter {


    public static final Region fromRegionRequestDtoToRegion(RegionRequestDto regionRequestDto){
        return Region.builder()
                     .regionName(regionRequestDto.getRegionName())
                     .build();
    }

    public static final RegionRequestDto fromRegionToRegionRequestDto(Region region){
        return RegionRequestDto.builder()
                               .regionName(region.getRegionName())
                               .build();
    }
}
