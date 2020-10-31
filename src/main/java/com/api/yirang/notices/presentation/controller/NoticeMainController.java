package com.api.yirang.notices.presentation.controller;

import com.api.yirang.notices.application.advancedService.NoticeActivityService;
import com.api.yirang.notices.application.basicService.NoticeService;
import com.api.yirang.notices.presentation.dto.NoticeResponsesDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/apis/main/notices")
public class NoticeMainController {

    private final NoticeActivityService noticeActivityService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public NoticeResponsesDto getPageNotices(@Param("page") Integer pageNum){
        return noticeActivityService.findNoticesByPage(pageNum);
    }
}
