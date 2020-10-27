package com.api.yirang.auth.presentation.controller;

import com.api.yirang.auth.application.basicService.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/apis/register")
public class AdminController {

    //DI
    private final UserService userService;

    // 기존의 User에 있던 사람만 관리자로 갈 수 있음
    // /v1/apis/register/admin?user_id=user_id
    @PostMapping(value="/admin")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAdmin(@RequestParam(name = "user_id") Long userId){
        userService.registerAdmin(userId);
    }

    // 기존의 Admin에 관리 지역 추가
    // /v1/apis/register/
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registerRegion()
}
