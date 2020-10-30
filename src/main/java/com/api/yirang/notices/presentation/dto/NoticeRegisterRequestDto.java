package com.api.yirang.notices.presentation.dto;


import com.api.yirang.notices.presentation.dto.embeded.ActivityRegisterRequestDto;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@ToString
public class NoticeRegisterRequestDto {

    @NotBlank(message = "title is mandatory")
    private final String title;

    private final ActivityRegisterRequestDto activityRegisterRequestDto;


    public NoticeRegisterRequestDto() {
        this.title = null;
        this.activityRegisterRequestDto = null;
    }

}
