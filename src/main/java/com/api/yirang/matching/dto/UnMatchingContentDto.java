package com.api.yirang.matching.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class UnMatchingContentDto {

    private final String name;
    private final Long id;

    public UnMatchingContentDto() {
        this.name = null;
        this.id = null;
    }

    @Builder
    public UnMatchingContentDto(String name, Long id) {
        this.name = name;
        this.id = id;
    }
}
