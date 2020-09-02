package com.api.yirang.web.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
public class RequestDto implements Serializable {
    private final String accessToken;
}

