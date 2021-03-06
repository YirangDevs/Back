package com.api.yirang.matching.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class MatchingRecordsDto {

    private final List<MatchingRecordDto> matchingRecordDtoList;

    public MatchingRecordsDto() {
        this.matchingRecordDtoList = null;
    }

    @Builder
    public MatchingRecordsDto(List<MatchingRecordDto> matchingRecordDtoList) {
        this.matchingRecordDtoList = matchingRecordDtoList;
    }
}
