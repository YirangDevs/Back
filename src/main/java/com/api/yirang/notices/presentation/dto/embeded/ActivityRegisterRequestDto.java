package com.api.yirang.notices.presentation.dto.embeded;


import com.api.yirang.common.support.type.Region;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@Getter
@ToString
@Builder
@AllArgsConstructor
public class ActivityRegisterRequestDto {

    @Min(value = 1, message = "nor should be larger than 0")
    private final Long nor; // number of request

    @Pattern(regexp = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$",
             message = "Date should be yyyy-mm-dd")
    private final String dov; // date of Volunteer

    @Pattern(regexp = "^(2[0-3]|[01]?[0-9]):([0-5]?[0-9]):([0-5]?[0-9])$",
             message = "Time should be hh:MM:ss")
    private final String tov; // time of Volunteer

    @Pattern(regexp = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$",
             message = "DateTime should be yyyy-mm-dd")
    private final String dod; // date of deadline

    private final Region region;

    private final String content;

    public ActivityRegisterRequestDto() {
        this.nor = null;
        this.dov = null;
        this.tov = null;
        this.dod = null;
        this.region = null;
        this.content = null;
    }

}
