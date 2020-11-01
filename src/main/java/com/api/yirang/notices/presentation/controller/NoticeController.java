package com.api.yirang.notices.presentation.controller;


import com.api.yirang.notices.presentation.dto.NoticeRegisterRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/apis/manage/notices")
public class NoticeController {

    // DI 할 필드

    @PostMapping(consumes = "application/json")
    public void registerNotice(@RequestBody NoticeRegisterRequestDto noticeRequestDto){
    }
}
