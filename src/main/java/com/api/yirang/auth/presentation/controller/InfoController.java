package com.api.yirang.auth.presentation.controller;


import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.auth.presentation.dto.UserInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/apis/info")
@RequiredArgsConstructor
public class InfoController {

    // Serivces DI
    private final UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value="/users/{userId}", produces = "application/json")
    public UserInfoResponseDto getUserInfo(@PathVariable("userId") String userId){
        System.out.println("[InfoController] User정보 요청 왔습니다.");
        return userService.findUserInfoByUserId(Long.parseLong(userId));
    }
}
