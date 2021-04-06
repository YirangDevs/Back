package com.api.yirang.matching.controller;


import com.api.yirang.matching.application.MatchingCrudService;
import com.api.yirang.matching.application.MatchingService;
import com.api.yirang.matching.dto.MatchingRecordsDto;
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
    @GetMapping(value = "/activities/{activity_id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public MatchingResponseDto getMatchingRecordsByActivityId(@PathVariable("activity_id") Long activityId){
        System.out.println("[MatchingController] 매칭 조회가 왔습니다.");
        return matchingService.findMatchingByActivityId(activityId);
    }



    /**
     * 목적: 해당 activity의 제외된 피봉사자 확인
     * 사용자: 슈퍼 관리자, 관리자
     */
    @GetMapping(value= "/unmatched/activities/{activity_id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public UnMatchingResponseDto getUnMatchingRecordsByActivityId(@PathVariable("activity_id") Long activityId){
        System.out.println("[MatchingController] 매칭 실패 조회가 왔습니다.");
        return matchingService.findUnMatchingByActivityId(activityId);
    }

    /**
     * 목적: 내가 이전에 진행했던 과거 매칭기록 또는 현재 기록?
     * 사용자: 봉사자
     */
    @GetMapping(value = "/users/{user_id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public MatchingRecordsDto getMyMatchingRecords(@PathVariable("user_id") Long userId){
        System.out.println("[MatchingController] 나의 매칭 기록 조회가 왔습니다.");
        return matchingService.findMyMatchingRecordsByUserId(userId);
    }
}
