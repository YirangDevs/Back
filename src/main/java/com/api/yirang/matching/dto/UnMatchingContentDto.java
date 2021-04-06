package com.api.yirang.matching.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UnMatchingContentDto {

    private String name;
    private Long id;

    public UnMatchingContentDto() {
        this.name = null;
        this.id = null;
    }
}
