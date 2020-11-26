package com.api.yirang.auth.presentation.controller;

import com.api.yirang.auth.application.advancedService.AuthService;
import com.api.yirang.auth.presentation.VO.RefreshResponseVO;
import com.api.yirang.auth.presentation.VO.SignInResponseVO;
import com.api.yirang.auth.presentation.dto.SignInRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/apis/auth")
public class AuthController {

    // Service DI
    private final AuthService authService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/signin", consumes = "application/json")
    public void signIn(@RequestBody SignInRequestDto signInRequestDto, HttpServletResponse response){
        System.out.println("[AuthController]: 로그인 요청을 받았 습니다.");
        SignInResponseVO signInResponseVO = authService.signin(signInRequestDto);
        response.addHeader("Authorization", "Bearer " + signInResponseVO.getYirangAccessToken());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/refresh")
    public void refreshToken(@RequestHeader("Authorization") String header, HttpServletResponse response){
        System.out.println("[AuthController]: 토큰을 Refresh 하는 요청을 받았 습니다.");
        RefreshResponseVO refreshResponseVO = authService.refresh(header);
        response.addHeader("Authorization", "Bearer " + refreshResponseVO.getYirangAccessToken());
    }

}
