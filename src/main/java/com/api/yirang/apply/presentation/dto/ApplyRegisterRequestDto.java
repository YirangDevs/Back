package com.api.yirang.apply.presentation.dto;

import com.api.yirang.seniors.support.custom.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by JeongminYoo on 2020/12/30
 * Work with Yirang
 * Email: likemin0142@gmail.com
 * Blog: http://Beewoom.github.io
 * Github: http://github.com/Biewoom
 */
@Getter
@ToString
@AllArgsConstructor
@Builder
public class ApplyRegisterRequestDto {

    // Fields
    private final Long noticeId;
    private final ServiceType serviceType;

    // NoArgsConstructor
    public ApplyRegisterRequestDto() {
        this.noticeId = null;
        this.serviceType = null;
    }
}
