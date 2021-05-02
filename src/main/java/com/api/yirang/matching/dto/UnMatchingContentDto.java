package com.api.yirang.matching.dto;

import com.api.yirang.common.support.type.Sex;
import lombok.Builder;
import lombok.Data;

@Data
public class UnMatchingContentDto {

    private final String name;
    private final Sex sex;
    private final String phone;
    private final String img;
    private final Long id;

    public UnMatchingContentDto() {
        this.sex = null;
        this.phone = null;
        this.img = null;
        this.name = null;
        this.id = null;
    }

    @Builder
    public UnMatchingContentDto(String name, Sex sex, String phone, String img, Long id) {
        this.name = name;
        this.sex = sex;
        this.phone = phone;
        this.img = img;
        this.id = id;
    }
}
