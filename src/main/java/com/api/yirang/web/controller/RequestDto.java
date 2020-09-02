package com.api.yirang.web.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto implements Serializable {
    private String access_token;

    @Override
    public String toString() {
        return "RequestDto{" +
               "accessToken='" + access_token + '\'' +
               '}';
    }
}

