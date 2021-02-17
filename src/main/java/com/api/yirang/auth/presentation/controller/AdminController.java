package com.api.yirang.auth.presentation.controller;

import com.api.yirang.auth.application.advancedService.AuthService;
import com.api.yirang.auth.application.basicService.AdminService;
import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.auth.domain.jwt.components.JwtParser;
import com.api.yirang.auth.presentation.VO.RefreshResponseVO;
import com.api.yirang.auth.presentation.dto.UserAuthResponseDto;
import com.api.yirang.auth.support.utils.ParsingHelper;
import com.api.yirang.common.support.custom.ValidCollection;
import com.api.yirang.common.support.type.Region;
import com.api.yirang.seniors.presentation.dto.response.SeniorResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/apis/admins")
@RequiredArgsConstructor
public class AdminController {

    // DI services
    private final AuthService authService;
    private final AdminService adminService;
    private final UserService userService;

    // DI Jwt
    private final JwtParser jwtParser;

    //
    /**
     * 목적: 전체 유저를 보여주는 API
     * 사용자: 슈퍼 관리자
     */
    @GetMapping(value = "/user-auths")
    @ResponseStatus(HttpStatus.OK)
    public final Map<String, Collection<UserAuthResponseDto>> getAllUserAuth(){
        System.out.println("[AdminController] 전체 유저 권한 리스트를 원하는 API 요청 받았습니다: ");
        Map<String, Collection<UserAuthResponseDto>> res = new HashMap<>();
        Collection<UserAuthResponseDto> userAuthResponseDtos = userService.findAllUserAuthInfos();
        res.put("userAuthorities", userAuthResponseDtos);
        return res;
    }

    /**
     * 목적: 기존의 봉사자 있던 사람 관리자로 승급
     * 사용자: 슈퍼 관리자
     */
    @PostMapping(value = "/{user_id}")
    @ResponseStatus(HttpStatus.CREATED)
    public final void registerAdmin(@PathVariable("user_id") Long userId){
        System.out.println("[AdminController]: 유저 권한 업그레이드를 원합니다");
        authService.changeToAdmin(userId);
    }

    /**
     * 목적: 기존의 관리자 있던 사람 봉사자로 강등
     * 사용자: 슈퍼 관리자
     */
    @DeleteMapping(value = "/{user_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public final void deleteAdmin(@PathVariable("user_id") Long userId){
        System.out.println("[AdminController]: 유저 권한 다운그레이드를 원합니다.");
        authService.changeToVolunteer(userId);
    }

    /**
     * 목적: 관리자의 관리 지역 업데이트
     * 사용자: 슈퍼 관리자
     */
    @PutMapping(value="/{user_id}")
    @ResponseStatus(HttpStatus.OK)
    public final void updateAdminRegions(@PathVariable("user_id") Long userId,
                                         @RequestBody List<Region> regions){
        System.out.println("[AdminController]: 관리자 구역 업데이트를 원합니다.");
        adminService.updateRegionsByUserId(userId, regions);
    }


    /**
     * 목적: 자신의 Admin 관리 지역 조회
     * 사용자: 슈퍼 관리자 & 관리자
     */
    @GetMapping(value="/region", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public final Map<String, Collection> getMyRegions(@RequestHeader("Authorization") String header){
        Map<String, Collection> res = new HashMap<>();
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        res.put("regions", adminService.findAreasByUserId(userId)); // {regions: ["중구", "동구"]}
        return res;
    }

    /**
     * 목적: 자신의 권한 조회
     * 사용자: 로그인한 사용자
     */
    @GetMapping(value = "/authority", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> getMyAuthority(@RequestHeader("Authorization") String header){
        Map<String, String> res = new HashMap<>();
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        res.put("authority", userService.getAuthorityByUserId(userId).toString());
        return res;
    }


}
