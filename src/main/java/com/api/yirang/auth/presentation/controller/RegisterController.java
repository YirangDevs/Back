package com.api.yirang.auth.presentation.controller;

import com.api.yirang.auth.application.basicService.UserService;
import com.api.yirang.auth.presentation.dto.RegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/apis/register")
public class RegisterController {

    private final UserService userService;

    @PostMapping(value="/admin")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAdmin(@RequestBody RegisterDto registerDto){
        userService.registerAdmin(registerDto);
    }
}
