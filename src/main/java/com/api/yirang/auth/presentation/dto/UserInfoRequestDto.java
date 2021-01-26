package com.api.yirang.auth.presentation.dto;

import com.api.yirang.common.support.type.Sex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

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
@AllArgsConstructor
@Builder
public class UserInfoRequestDto {

    @NotNull
    @NotEmpty
    private final String username;

    @NotNull
    @Pattern(regexp = "[0-9]*$",
             message = "Phone should be numbers!")
    private final String phone;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$",
             message = "Email should be email type!")
    private final String email;

    public UserInfoRequestDto(){
        this.username = null;
        this.phone = null;
        this.email = null;
    }
}
