package com.api.yirang.auth.presentation.controller;

import com.api.yirang.auth.application.advancedService.AuthService;
import com.api.yirang.auth.presentation.VO.RefreshResponseVO;
import com.api.yirang.auth.presentation.VO.SignInResponseVO;
import com.api.yirang.auth.presentation.dto.SignInRequestDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/apis/auth")
@Api(tags = "로그인과 토큰 재발급을 담당하는 API")
public class AuthController {

    // Service DI
    private final AuthService authService;

    @ApiOperation(value = "로그인 입니다.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/signin", consumes = "application/json")
    public void signIn(@RequestBody SignInRequestDto signInRequestDto, HttpServletResponse response){
        System.out.println("[AuthController]: 로그인 요청을 받았 습니다.");
        SignInResponseVO signInResponseVO = authService.signin(signInRequestDto);
        response.addHeader("Authorization", "Bearer " + signInResponseVO.getYirangAccessToken());
    }

    @ApiOperation(value = "Refresh 입니다.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/refresh")
    public void refreshToken(@RequestHeader("Authorization") String header, HttpServletResponse response){
        System.out.println("[AuthController]: 토큰을 Refresh 하는 요청을 받았 습니다.");
        RefreshResponseVO refreshResponseVO = authService.refresh(header);
        response.addHeader("Authorization", "Bearer " + refreshResponseVO.getYirangAccessToken());
    }

}
