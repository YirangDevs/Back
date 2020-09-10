package com.api.yirang.auth.presentation.controller;


import com.api.yirang.auth.application.basicService.UserService;
import com.api.yirang.auth.presentation.dto.UserInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/apis/info")
@RequiredArgsConstructor
public class InfoController {

    // Serivces DI
    private UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value="/users/{userId}", produces = "application/json")
    public UserInfoResponseDto getUserInfo(@PathVariable Long userId){
        return userService.findUserInfoByUserId(userId);
    }
}
