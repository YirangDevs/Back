package com.api.yirang.notices.presentation.controller;


import com.api.yirang.notices.application.advancedService.NoticeActivityService;
import com.api.yirang.notices.presentation.dto.NoticeRegisterRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/apis/manage/notices")
public class NoticeController {

    // DI 할 필드
    private final NoticeActivityService noticeActivityService;

    @PostMapping(consumes = "application/json")
    public void registerNotice(@RequestHeader("Authorization") String header, @Valid @RequestBody NoticeRegisterRequestDto noticeRequestDto){
        noticeActivityService.register(header, noticeRequestDto);
    }
}
