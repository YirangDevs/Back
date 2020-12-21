package com.api.yirang.seniors.presentation.dto.request;


import com.api.yirang.common.support.type.Region;
import com.api.yirang.common.support.type.Sex;
import com.api.yirang.seniors.support.custom.ServiceType;
import lombok.*;

import javax.validation.constraints.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
public class RegisterSeniorRequestDto {

    @NotBlank
    private final String name;

    private final Region region;
    private final String address;

    @Pattern(regexp = "^[0-9]*$",
             message = "Phone should be numbers!")
    private final String phone;

    private final Sex sex;
    private final ServiceType type;

    @Pattern(regexp = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])",
             message = "Date should be yyyy-mm-dd")
    private final String date;

    @Min(value = 0)
    private final Long priority;

    private final Long numsOfRequiredVolunteers;

    public RegisterSeniorRequestDto() {
        this.name = null;
        this.region = null;
        this.address = null;
        this.phone = null;
        this.sex = null;
        this.type = null;
        this.date = null;
        this.priority = null;
        this.numsOfRequiredVolunteers = null;
    }

    public RegisterSeniorRequestDto(RegisterTotalSeniorRequestDto registerTotalSeniorRequestDto) {
        this.name = registerTotalSeniorRequestDto.getName();
        this.region = registerTotalSeniorRequestDto.getRegion();
        this.address = registerTotalSeniorRequestDto.getAddress();
        this.phone = registerTotalSeniorRequestDto.getPhone();
        this.sex = registerTotalSeniorRequestDto.getSex();
        this.type = registerTotalSeniorRequestDto.getType();
        this.date = registerTotalSeniorRequestDto.getDate();
        this.priority = registerTotalSeniorRequestDto.getPriority();
        this.numsOfRequiredVolunteers = Long.valueOf(2);
    }
}
