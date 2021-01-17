package com.api.yirang.seniors.presentation.dto.request;

import com.api.yirang.common.support.type.Region;
import com.api.yirang.common.support.type.Sex;
import com.api.yirang.seniors.support.custom.ServiceType;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Created by JeongminYoo on 2020/11/28
 * Work with Yirang
 * Email: likemin0142@gmail.com
 * Blog: http://Beewoom.github.io
 * Github: http://github.com/Biewoom
 */
@Getter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class RegisterTotalSeniorRequestDto {

    @NotNull
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

    public RegisterTotalSeniorRequestDto() {
        this.name = null;
        this.region = null;
        this.address = null;
        this.phone = null;
        this.sex = null;
        this.type = null;
        this.date = null;
        this.priority = null;
    }
}
