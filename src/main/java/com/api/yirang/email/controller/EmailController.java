package com.api.yirang.email.controller;


import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.auth.domain.jwt.components.JwtParser;
import com.api.yirang.auth.support.utils.ParsingHelper;
import com.api.yirang.email.application.EmailService;
import com.api.yirang.email.dto.EmailRequestDto;
import com.api.yirang.email.dto.EmailValidationRequestDto;
import com.api.yirang.email.dto.EmailValidationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/apis/emails")
@RequiredArgsConstructor
public class EmailController {

    // DI services
    private final EmailService emailService;
    private final UserService userService;

    // DI jwt
    private final JwtParser jwtParser;

    /**
     * 목적: 자신의 Email 주소를 바꾸는 API
     * 사용자: 슈퍼 관리자, 관리자, 봉사자
     */
    @PutMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void changeMyEmail(@RequestHeader("Authorization") String header,
                              @RequestBody @Valid EmailRequestDto emailRequestDto){
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        userService.updateMyEmail(userId, emailRequestDto);
    }

    /**
     *  목적: 자신의 Email 검증이 됬는지 알아보는 API
     *  사용자: 슈퍼 관리자, 관리자, 봉사자
     */
    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public EmailValidationResponseDto checkValidatedEmail(@RequestHeader("Authorization") String header){
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        return emailService.checkMyValidation(userId);
    }

    /**
     * 목적: 자신의 Email 검증하기 위해 인증번호를 입력하는 API
     * 사용자: 슈퍼 관리자, 관리자, 봉사자
     * @param header Authorization JWT token
     * @param emailValidationRequestDto 인증번호가 담긴 DTO
     */
    @PostMapping(value = "/validation", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void verifyCertificationEmail(@RequestHeader("Authorization") String header,
                                         @RequestBody EmailValidationRequestDto emailValidationRequestDto){
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        emailService.verifyMyEmail(userId, emailValidationRequestDto);
    }

    /**
     * 목적: 자신의 이메일 인증번호 생성을 위한 API
     * 사용자: 슈퍼관리자, 관리자, 봉사자
     */
    @PostMapping(value="/certification")
    @ResponseStatus(HttpStatus.CREATED)
    public void makeCertificateEmail(@RequestHeader("Authorization") String header){
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        emailService.sendVerificationEmail(userId);
    }

}
