package com.api.yirang.apply.presentation.dto;

import com.api.yirang.common.support.type.Sex;
import com.api.yirang.seniors.support.custom.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by JeongminYoo on 2021/1/6
 * Work with Yirang
 * Email: likemin0142@gmail.com
 * Blog: http://Beewoom.github.io
 * Github: http://github.com/Biewoom
 */
@Getter
@ToString
@AllArgsConstructor
@Builder
public class ApplicantResponseDto {

    // Fields
    private final Sex sex;
    private final String name;
    private final ServiceType serviceType;
    private final String email;

    // 프사를 주려고 해도.. 없다...

    public ApplicantResponseDto() {
        this.sex = null;
        this.name = null;
        this.serviceType = null;
        this.email = null;
    }
}
