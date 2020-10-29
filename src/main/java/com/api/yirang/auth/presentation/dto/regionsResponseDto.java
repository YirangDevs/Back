package com.api.yirang.auth.presentation.dto;


import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class regionsResponseDto {

    private List<String> regions;

    @Builder
    public regionsResponseDto(List<String> regions) {
        this.regions = regions;
    }

    public regionsResponseDto() {
        this.regions = null;
    }
}
