package com.api.yirang.auth.presentation.controller;


import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.auth.domain.jwt.components.JwtParser;
import com.api.yirang.auth.presentation.dto.UserInfoRequestDto;
import com.api.yirang.auth.presentation.dto.UserInfoResponseDto;
import com.api.yirang.auth.support.utils.ParsingHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


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

    // Update
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/myinfo", consumes = "application/json")
    public void updateMyInfo(@RequestHeader("Authorization") String header,
                             @RequestBody UserInfoRequestDto userInfoRequestDto){
        System.out.println("[InfoController] 내 정보 업데이트 요청이 왔습니다. ");
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        userService.updateUserInfoWithUserId(userId, userInfoRequestDto);
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
