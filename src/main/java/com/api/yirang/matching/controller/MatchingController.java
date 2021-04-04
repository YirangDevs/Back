package com.api.yirang.matching.controller;


import com.api.yirang.matching.application.MatchingCrudService;
import com.api.yirang.matching.application.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/apis/matchings")
public class MatchingController {

    // DI Service
    private final MatchingService matchingService;
    private final MatchingCrudService matchingCrudService;

    /**
     * 목적: 해당 activity의 매칭된 기록 확인
     * 사용자: 슈퍼 관리자, 관리자
     */


    /**
     * 목적: 해당 activity의 제외된 피봉사자 확인
     * 사용자: 슈퍼 관리자, 관리자
     */
}
