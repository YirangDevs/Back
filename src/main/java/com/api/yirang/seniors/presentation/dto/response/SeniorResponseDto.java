package com.api.yirang.seniors.presentation.dto.response;


import com.api.yirang.common.support.type.Sex;
import com.api.yirang.seniors.support.custom.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@Builder
public class SeniorResponseDto {

    private final Long id;
    private final String name;

    private final Sex sex;
    private final String region;

    private final String address;
    private final String phone;
    private final ServiceType type;

    private final String date;
    private final Long priority;

    public SeniorResponseDto() {
        this.id = null;
        this.name = null;
        this.sex = null;
        this.region = null;
        this.address = null;
        this.phone = null;
        this.type = null;
        this.date = null;
        this.priority = null;
    }
}
