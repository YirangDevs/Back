package com.api.yirang.email.dto;

import com.api.yirang.common.support.time.TimeConverter;
import com.api.yirang.common.support.type.Region;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MatchingMailContent {

    private final String dtov;
    private final Region region;
    private final String seniorName;
    private final String phoneNumber;

    @Builder
    public MatchingMailContent(LocalDateTime dtov, Region region, String seniorName, String phoneNumber) {
        this.dtov = TimeConverter.LocalDateTimeToMailContentString(dtov);
        this.region = region;
        this.seniorName = seniorName;
        this.phoneNumber = phoneNumber;
    }
}
