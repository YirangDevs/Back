package com.api.yirang.matching.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UnMatchingResponseDto {

    private final List<UnMatchingContentDto> unMatchedSeniors;
    private final List<UnMatchingContentDto> unMatchedVolunteers;


    public UnMatchingResponseDto() {
        this.unMatchedSeniors = null;
        this.unMatchedVolunteers = null;
    }
}
