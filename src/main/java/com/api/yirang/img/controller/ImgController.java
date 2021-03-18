package com.api.yirang.img.controller;

import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.auth.domain.jwt.components.JwtParser;
import com.api.yirang.img.application.ImgService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/apis/imgs")
public class ImgController {

    // DI services
    private final UserService userService;
    private final ImgService imgService;

    // DI Jwt
    private final JwtParser jwtParser;


    //TODO: Custom img 업로드 하는 API
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void

    //TODO: ImgType 바꾸는 API
    @PutMapping(value = "/imgTypes", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void changeImgType(@RequestHeader("Authorization"))

    //TODO: 자신의 ImgType을 알 수 있는 API

    //TODO: 자신의 Img를 알 수 있는 API
}
