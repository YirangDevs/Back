package com.api.yirang.email.controller;


import com.api.yirang.auth.domain.jwt.components.JwtParser;
import com.api.yirang.auth.support.utils.ParsingHelper;
import com.api.yirang.email.application.EmailService;
import com.api.yirang.email.dto.EmailRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@RestController
@RequestMapping("/v1/apis/emails")
@RequiredArgsConstructor
public class EmailController {

    // DI services
    private final EmailService emailService;

    // DI jwt
    private final JwtParser jwtParser;

    /**
     * 목적: 자신의 Email 주소를 바꾸는 API
     * 사용자: 슈퍼 관리자, 관리자, 봉사자
     */
    @PutMapping(consumes = "application/json")


    /**
     *  목적: 자신의 Email 검증이 됬는지 알아보는 API
     *  사용자: 슈퍼 관리자, 관리자, 봉사자
     */
    @GetMapping()


    /**
     * 목적: 자신의 이메일 인증을 위한 API
     * 사용자: 슈퍼관리자, 관리자, 봉사자
     */
    @PostMapping(value="/certification")
    @ResponseStatus(HttpStatus.OK)
    public void certificateEmail(@RequestHeader("Authorization") String header){
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        emailService.sendVerificationEmail(userId);
    }


}
