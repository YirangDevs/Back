package com.api.yirang.auth.presentation.dto;

import com.api.yirang.common.support.type.Region;
import com.api.yirang.common.support.type.Sex;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Created by JeongminYoo on 2021/1/18
 * Work with Yirang
 * Email: likemin0142@gmail.com
 * Blog: http://Beewoom.github.io
 * Github: http://github.com/Biewoom
 */
@Getter
@ToString
@Builder
@RequiredArgsConstructor
public class UserInfoRequestDto {

    @NotNull
    @NotEmpty
    private final String username;

    @NotNull
    @NotEmpty
    private final String realname;

    @NotNull
    private final Sex sex;


    @NotNull
    @Pattern(regexp = "[0-9]*$",
             message = "Phone should be numbers!")
    private final String phone;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$",
             message = "Email should be email type!")
    private final String email;

    private final Region firstRegion;
    private final Region secondRegion;

    public UserInfoRequestDto(){
        this.username = null;
        this.realname = null;
        this.sex = null;
        this.phone = null;
        this.email = null;

        this.firstRegion = null;
        this.secondRegion = null;
    }
}
