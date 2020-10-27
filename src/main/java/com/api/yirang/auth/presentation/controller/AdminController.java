package com.api.yirang.auth.presentation.controller;

import com.api.yirang.auth.application.intermediateService.AdminRegionService;
import com.api.yirang.auth.application.intermediateService.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/apis/register")
public class AdminController {

    // DI services
    private final UserService userService;
    private final AdminRegionService adminRegionService;
    
    // 기존의 User에 있던 사람만 관리자로 갈 수 있음
    // /v1/apis/register/admin?user_id=user_id
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAdmin(@RequestParam(name = "user_id") Long userId){
        userService.registerAdmin(userId);
    }

    // 기존의 Admin에 관리 지역 추가
    // /v1/apis/register/region
    @PostMapping(value="/{userId}/{regionName}", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerRegion(@PathVariable Long userId, @PathVariable String regionName){
        adminRegionService.delegateRegion(userId, regionName);
    }
}
