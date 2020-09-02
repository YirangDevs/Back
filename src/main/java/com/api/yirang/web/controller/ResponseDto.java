package com.api.yirang.web.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto implements Serializable {
    private String nickname;

    @Override
    public String toString() {
        return "ResponseDto{" +
               "nickname='" + nickname + '\'' +
               '}';
    }
}
