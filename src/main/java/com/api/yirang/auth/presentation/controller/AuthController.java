package com.api.yirang.auth.presentation.controller;

import com.api.yirang.auth.application.advancedService.AuthService;
import com.api.yirang.auth.presentation.VO.SignInResponseVO;
import com.api.yirang.auth.presentation.dto.SignInRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/v1/apis/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/signin", consumes = "application/json")
    public void signIn(@RequestBody SignInRequestDto signInRequestDto, HttpServletResponse response){
        SignInResponseVO signInResponseVO = authService.signin(signInRequestDto);
        response.addHeader("Authorization", "Bearer " + signInResponseVO.getYirangAccessToken());
    }


    //TO-DO 갱신

}
