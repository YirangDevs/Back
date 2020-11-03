package com.api.yirang.seniors.presentation.dto.request;


import com.api.yirang.common.support.type.Sex;
import com.api.yirang.seniors.support.custom.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
public class RegisterSeniorRequestDto {

    @NotBlank
    private final String name;

    private final String region;
    private final String address;

    @Pattern(regexp = "^[0-9]*$")
    private final String phone;

    private final Sex sex;
    private final ServiceType type;

    @Pattern(regexp = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])",
             message = "Date should be yyyy-mm-dd")
    private final String date;

    @Min(value = 0)
    private final Long priority;
}
