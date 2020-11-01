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

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/apis/main/notices")
public class NoticeMainController {

    private final NoticeActivityService noticeActivityService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public NoticeResponsesDto getPageNotices(@Param("page") Integer pageNum){
        System.out.println("[NoticeMainController] 공고 페이지 조회 요청이 왔습니다.");
        return noticeActivityService.findNoticesByPage(pageNum);
    }

    @GetMapping(value = "/nums", produces = "application/json")
    @ResponseStatus
    public Map<String, Long> getNumsOfNotices(){
        System.out.println("[NoticeMainController] 공고 숫자 요청이 왔습니다.");
        Map<String, Long> res = new HashMap<>();
        res.put("totalNoticeNums", noticeActivityService.findNumsOfNotices());
        return res;
    }
}
