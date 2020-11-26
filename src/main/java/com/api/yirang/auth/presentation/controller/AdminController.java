package com.api.yirang.auth.presentation.controller;

import com.api.yirang.auth.application.advancedService.AuthService;
import com.api.yirang.auth.application.basicService.AdminService;
import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.auth.domain.jwt.components.JwtParser;
import com.api.yirang.auth.presentation.VO.RefreshResponseVO;
import com.api.yirang.auth.support.utils.ParsingHelper;
import com.api.yirang.common.support.type.Region;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/v1/apis/admins")
@RequiredArgsConstructor
public class AdminController {

    // DI services
    private final AuthService authService;
    private final AdminService adminService;

    // DI Jwt
    private final JwtParser jwtParser;

    // 기존의 User에 있던 사람만 관리자로 갈 수 있음
    // /v1/apis/admin
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public final void registerAdmin(@RequestHeader("Authorization") String header, HttpServletResponse response){
        System.out.println("[AdminController]: 유저 권한 업그레이드를 원합니다");
        RefreshResponseVO refreshResponseVO = authService.refreshToAdmin(header);
        response.setHeader("Authorization", "Bearer " + refreshResponseVO.getYirangAccessToken());
    }

    // 기존의 Admin이 었던 사람이 일반 유저로 바꾸는 경우
    // /v1/apis/admin
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public final void deleteAdmin(@RequestHeader("Authorization") String header, HttpServletResponse response){
        System.out.println("[AdminController]: 유저 권한 다운그레이드를 원합니다.");
        RefreshResponseVO refreshResponseVO = authService.refreshFromAdmin(header);
        response.setHeader("Authorization", "Bearer " + refreshResponseVO.getYirangAccessToken());
    }

    // 기존의 Admin에 관리 지역 추가
    // /v1/apis/admin/region
    @PostMapping(value="/region/{region}")
    @ResponseStatus(HttpStatus.CREATED)
    public final void registerMyRegion(@RequestHeader("Authorization") String header, @PathVariable @Valid Region region){
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        adminService.addAreaByUserId(userId, region);
    }
    // 기존의 Admin에 관리 지역 삭제
    // /v1/apis/admin/region
    @DeleteMapping(value="/region/{region}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public final void deleteMyRegion(@RequestHeader("Authorization") String header, @PathVariable @Valid Region region){
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        adminService.deleteAreaByUserId(userId, region);
    }
    // 기존 Admin 관리 지역 조회
    // /v1/apis/admin/region
    @GetMapping(value="/region", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public final Map<String, Collection> getMyRegions(@RequestHeader("Authorization") String header){
        Map<String, Collection> res = new HashMap<>();
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        res.put("regions", adminService.findAreasByUserId(userId));
        return res;
    }



}
