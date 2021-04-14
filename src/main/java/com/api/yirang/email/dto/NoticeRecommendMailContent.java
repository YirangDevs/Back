package com.api.yirang.email.dto;

import com.api.yirang.common.support.time.TimeConverter;
import com.api.yirang.common.support.type.Region;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoticeRecommendMailContent {

    private final String region;
    private final String noticeTitle;
    private final String nor;
    private final String dod;
    private final String dov;
    private final String tov;


    @Builder
    public NoticeRecommendMailContent(Region region, String noticeTitle,
                                      LocalDateTime dtov, LocalDateTime dtod,
                                      Long nor) {

        this.region = region.getRegionName();
        this.noticeTitle = noticeTitle;
        this.dod = TimeConverter.LocalDateTimeToMailContentStringV2(dtod);
        this.dov = TimeConverter.LocalDateTimeToMailContentStringV2(dtov);
        this.tov = TimeConverter.LocalDateTimeToMailContentStringV3(dtov);
        this.nor = nor.toString();
    }
}
