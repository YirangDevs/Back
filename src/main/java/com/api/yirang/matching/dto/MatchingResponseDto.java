package com.api.yirang.matching.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class MatchingResponseDto {

    private final List<MatchingContentDto> matchingContentDtos;

    public MatchingResponseDto() {
        this.matchingContentDtos = null;
    }


    @Builder
    public MatchingResponseDto(List<MatchingContentDto> matchingContentDtos) {
        this.matchingContentDtos = matchingContentDtos;
    }
}
