package com.api.yirang.img.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GeoCode {

    private final String xCode;
    private final String yCode;

    @Builder
    public GeoCode(String xCode, String yCode) {
        this.xCode = xCode;
        this.yCode = yCode;
    }
}
