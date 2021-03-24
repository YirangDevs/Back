package com.api.yirang.notices.presentation.controller;

import com.api.yirang.notices.application.advancedService.NoticeActivityService;
import com.api.yirang.notices.application.basicService.ActivityBasicService;
import com.api.yirang.notices.presentation.dto.ActivitiesPagingResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/apis/manage/activities")
public class ActivityController {

    private final ActivityBasicService activityBasicService;
    private final NoticeActivityService noticeActivityService;

    /**
     * 목적: 공고 페이징 조회하는 API
     * 사용자: Admin, Super_Admin
     */
    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ActivitiesPagingResponseDto getPageActivites (@RequestHeader("Authorization") String header,
                                                         @Param("page") @Min(value = 0) Integer page){

    }

    /**
     * 목적: 공고 하나를 조회하는 API
     * 사용자: Admin, Super_Admin
     */
    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public

    /**
     * 목적: 공고글의 갯수를 조회하는 API
     * 사용자: Admin, Super_Admin
     */
}
