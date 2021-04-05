package com.api.yirang.matching.controller;


import com.api.yirang.matching.application.MatchingCrudService;
import com.api.yirang.matching.application.MatchingService;
import com.api.yirang.matching.dto.MatchingResponseDto;
import com.api.yirang.matching.dto.UnMatchingResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping(value = "/activity/{activity_id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public MatchingResponseDto getMatchingRecordsByActivityId(@PathVariable("activity_id") Long activityId){
        System.out.println("[MatchingController] 매칭 조회가 왔습니다.");
        return matchingService.findMatchingByActivityId(activityId);
    }



    /**
     * 목적: 해당 activity의 제외된 피봉사자 확인
     * 사용자: 슈퍼 관리자, 관리자
     */
    @GetMapping(value= "unmatched/activity/{activity_id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public UnMatchingResponseDto getUnMatchingRecordsByActivityId(@PathVariable("activity_id") Long activityId){
        System.out.println("[MatchingController] 매칭 조회가 왔습니다.");
        return matchingService.findUnMatchingByActivityId(activityId);
        //TODO: Service
    }
}
