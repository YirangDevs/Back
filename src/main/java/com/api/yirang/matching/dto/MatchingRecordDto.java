package com.api.yirang.matching.dto;

import com.api.yirang.common.support.type.Region;
import com.api.yirang.seniors.support.custom.ServiceType;
import lombok.Builder;
import lombok.Data;

@Data
public class MatchingRecordDto {

    private final String dtov;
    private final String dtom;

    private final Region region;
    private final ServiceType serviceType;

    public MatchingRecordDto() {
        this.dtov = null;
        this.dtom = null;
        this.region = null;
        this.serviceType = null;
    }

    @Builder
    public MatchingRecordDto(String dtov, String dtom, Region region, ServiceType serviceType) {
        this.dtov = dtov;
        this.dtom = dtom;
        this.region = region;
        this.serviceType = serviceType;
    }
}
