package com.api.yirang.matching.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MatchingResponseDto {

    private final List<MatchingContentDto> matchingContentDtos;

    public MatchingResponseDto() {
        this.matchingContentDtos = null;
    }
}
