package com.api.yirang.auth.presentation.dto;

import com.api.yirang.common.support.type.Sex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

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

    private final String username;
    private final String phone;
    private final String email;

    public UserInfoRequestDto(){
        this.username = null;
        this.phone = null;
        this.email = null;
    }
}
