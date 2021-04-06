package com.api.yirang.matching.dto;

import com.api.yirang.common.support.type.Region;
import com.api.yirang.seniors.support.custom.ServiceType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
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
}
