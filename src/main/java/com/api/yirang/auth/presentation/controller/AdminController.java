package com.api.yirang.auth.presentation.controller;

import com.api.yirang.auth.application.intermediateService.AdminRegionService;
import com.api.yirang.auth.application.intermediateService.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/apis/admin")
public class AdminController {

    // DI services
    private final UserService userService;
    private final AdminRegionService adminRegionService;
    
    // 기존의 User에 있던 사람만 관리자로 갈 수 있음
    // /v1/apis/admin?user_id=user_id
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAdmin(@RequestParam(name = "user_id") Long userId){
        userService.registerAdmin(userId);
    }

    // 기존의 Admin에 관리 지역 추가
    // /v1/apis/admin/region
    @PostMapping(value="/region/{userId}/{regionName}", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerRegion(@PathVariable Long userId, @PathVariable String regionName){
        adminRegionService.delegateRegion(userId, regionName);
    }

    // 기존의 Admin이 었던 사람이 일반 유저로 바꾸는 경우
    // /v1/apis/admin?user_id=user_id
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAdmin(@RequestParam(name = "user_id") Long userId){
        userService.fireAdmin(userId);
    }

}
