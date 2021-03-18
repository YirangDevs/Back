package com.api.yirang.auth.presentation.controller;


import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.auth.domain.jwt.components.JwtParser;
import com.api.yirang.auth.presentation.dto.UserInfo.*;
import com.api.yirang.auth.presentation.dto.UserInfoResponseDto;
import com.api.yirang.auth.support.utils.ParsingHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * 사용자의 정보를 조회 하거나, 수정하는 Controller
 */
@RestController
@RequestMapping("/v1/apis/info")
@RequiredArgsConstructor
public class InfoController {

    // Serivces DI
    private final UserService userService;
    private final JwtParser jwtParser;

    // Super Admin 만 가능
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value="/users/{userId}", produces = "application/json")
    public UserInfoResponseDto getUserInfo(@PathVariable("userId") Long userId){
        System.out.println("[InfoController] User 정보 요청 왔습니다.");
        return userService.findUserInfoByUserId(userId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/users/{userId}")
    public void kickUser(@PathVariable("userId") Long userId){
        System.out.println("[InfoController] User 삭제 요청이 왔습니다.");
        userService.deleteUser(userId);
    }

    // Get
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/myinfo", produces = "application/json")
    public UserInfoResponseDto getMyInfo(@RequestHeader("Authorization") String header){
        System.out.println("[InfoController] 내 정보 조회 요청이 왔습니다." );
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        return userService.findUserInfoByUserId(userId);
    }

    /** Update **/
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/myinfo/username", consumes = "application/json")
    public void updateMyUserName(@RequestHeader("Authorization") String header,
                                 @RequestBody @Valid UsernameUpdateRequestDto usernameUpdateRequestDto){
        System.out.println("[InfoController] Username 업데이트 요청이 왔습니다. ");
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        userService.updateUserInfoWithUserId(userId, usernameUpdateRequestDto);
    }
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/myinfo/realname", consumes = "application/json")
    public void updateMyRealname(@RequestHeader("Authorization") String header,
                                 @RequestBody @Valid RealnameUpdateRequestDto realnameUpdateRequestDto){
        System.out.println("[InfoController] Realname 업데이트 요청이 왔습니다. ");
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        userService.updateUserInfoWithUserId(userId, realnameUpdateRequestDto);
    }
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/myinfo/sex", consumes = "application/json")
    public void updateMySex(@RequestHeader("Authorization") String header,
                             @RequestBody @Valid SexUpdateRequestDto sexUpdateRequestDto){
        System.out.println("[InfoController] Sex 업데이트 요청이 왔습니다. ");
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        userService.updateUserInfoWithUserId(userId, sexUpdateRequestDto);

    }
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/myinfo/phone", consumes = "application/json")
    public void updateMyPhone(@RequestHeader("Authorization") String header,
                              @RequestBody @Valid PhoneUpdateRequestDto phoneUpdateRequestDto){
        System.out.println("[InfoController] Phone 업데이트 요청이 왔습니다. ");
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        userService.updateUserInfoWithUserId(userId, phoneUpdateRequestDto);

    }
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/myinfo/firstRegion", consumes = "application/json")
    public void updateMyFirstRegion(@RequestHeader("Authorization") String header,
                                    @RequestBody @Valid FirstRegionUpdateRequestDto firstRegionUpdateRequestDtoO){
        System.out.println("[InfoController] FirstRegion 업데이트 요청이 왔습니다. ");
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        userService.updateUserInfoWithUserId(userId, firstRegionUpdateRequestDtoO);

    }
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/myinfo/secondRegion", consumes = "application/json")
    public void updateMySecondRegion(@RequestHeader("Authorization") String header,
                                     @RequestBody @Valid SecondRegionUpdateRequestDto secondRegionUpdateRequestDto){
        System.out.println("[InfoController] SecondRegion 업데이트 요청이 왔습니다. ");
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        userService.updateUserInfoWithUserId(userId, secondRegionUpdateRequestDto);

    }

    // Delete
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/myinfo")
    public void deleteMyInfo(@RequestHeader("Authorization") String header){
        System.out.println("[InfoController] 내 정보 삭제 요청이 왔습니다.");
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        userService.deleteUser(userId);
    }


}
