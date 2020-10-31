package com.api.yirang.notices.presentation.dto;


import com.api.yirang.notices.presentation.dto.embeded.ActivityRegisterRequestDto;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@ToString
@AllArgsConstructor
@Builder
public class NoticeRegisterRequestDto {

    @NotBlank(message = "title is mandatory")
    @Length(min = 3, max = 100,
            message = "title should be between 5 ~ 100")
    private final String title;

    @Valid
    private final ActivityRegisterRequestDto activityRegisterRequestDto;

    public NoticeRegisterRequestDto() {
        this.title = null;
        this.activityRegisterRequestDto = null;
    }

}
