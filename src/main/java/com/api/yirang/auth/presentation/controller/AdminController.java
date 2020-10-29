package com.api.yirang.auth.presentation.controller;

import com.api.yirang.auth.application.advancedService.AuthService;
import com.api.yirang.auth.application.intermediateService.AdminRegionService;
import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.auth.domain.jwt.components.JwtParser;
import com.api.yirang.auth.presentation.VO.RefreshResponseVO;
import com.api.yirang.auth.presentation.dto.regionsResponseDto;
import com.api.yirang.auth.support.utils.ParsingHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/v1/apis/admins")
@RequiredArgsConstructor
public class AdminController {

    // DI services
    private final UserService userService;
    private final AdminRegionService adminRegionService;
    private final AuthService authService;

    // DI Jwt
    private final JwtParser jwtParser;

    // 기존의 User에 있던 사람만 관리자로 갈 수 있음
    // /v1/apis/admin
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAdmin(@RequestHeader("Authorization") String header, HttpServletResponse response){
        RefreshResponseVO refreshResponseVO = authService.refreshToAdmin(header);
        response.setHeader("Authorization", "Bearer " + refreshResponseVO.getYirangAccessToken());
    }

    // 기존의 Admin이 었던 사람이 일반 유저로 바꾸는 경우
    // /v1/apis/admin
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAdmin(@RequestHeader("Authorization") String header, HttpServletResponse response){
        RefreshResponseVO refreshResponseVO = authService.refreshFromAdmin(header);
        response.setHeader("Authorization", "Bearer " + refreshResponseVO.getYirangAccessToken());
    }

    // 기존의 Admin에 관리 지역 추가
    // /v1/apis/admin/region
    @PostMapping(value="/region/{regionName}")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerMyRegion(@RequestHeader("Authorization") String header, @PathVariable String regionName){
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        adminRegionService.delegateRegion(userId, regionName);
    }
    // 기존의 Admin에 관리 지역 삭제
    // /v1/apis/admin/region
    @DeleteMapping(value="/region/{regionName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMyRegion(@RequestHeader("Authorization") String header, @PathVariable String regionName){
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        adminRegionService.unDelegateRegion(userId, regionName);
    }
    // 기존 Admin 관리 지역 조회
    // /v1/apis/admin/region
    @GetMapping(value="/region", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public regionsResponseDto getMyRegions(@RequestHeader("Authorization") String header){
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        return adminRegionService.getMyRegions(userId);
    }



}
