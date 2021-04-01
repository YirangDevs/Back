package com.api.yirang.notices.presentation.controller;

import com.api.yirang.auth.support.utils.ParsingHelper;
import com.api.yirang.notices.application.advancedService.NoticeActivityService;
import com.api.yirang.notices.application.advancedService.UserAdminActivityService;
import com.api.yirang.notices.application.basicService.ActivityBasicService;
import com.api.yirang.notices.presentation.dto.ActivityOneResponseDto;
import com.api.yirang.auth.domain.jwt.components.JwtParser;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;



@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/apis/manage/activities")
public class ActivityController {

    private final ActivityBasicService activityBasicService;
    private final UserAdminActivityService userAdminActivityService;
    private final JwtParser jwtParser;


    /**
     * 목적: 액티비티 페이징 조회하는 API
     * 사용자: Admin, Super_Admin
     */
    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Collection> getActivity (@Param("page") @Min(value = 0) Integer page, @RequestHeader("Authorization") String header){
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));

        System.out.println("[ActivityController] 액티비티 페이지 조회 요청이 왔습니다.");
        System.out.println("[ActivityController] PageNums: " + page);

        Map<String, Collection> res = new HashMap<>();
        res.put("activities", userAdminActivityService.getAllActivityByPage(page, userId));
        return res;
    }
    /**
     * 목적: 액티비티 하나를 조회하는 API
     * 사용자: Admin, Super_Admin
     */

    @GetMapping(value = "/{activity_id}",produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ActivityOneResponseDto getActivityById(@PathVariable("activity_id") Long activityId){
        System.out.println("[ActivityController] 액티비티 조회 요청이 왔습니다.");
        System.out.println("[ActivityController] activityId: " + activityId);
        ActivityOneResponseDto activityOneResponseDto = activityBasicService.getOneActivityById(activityId);
        return activityOneResponseDto;
    }




    /**
     * 목적: 액티비티의 갯수를 조회하는 API
     * 사용자: Admin, Super_Admin
     */
    @GetMapping(value = "/nums", produces="application/json")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Long> getActivityNum(@RequestHeader("Authorization") String header){
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        System.out.println("[ActivityController] 액티비티 숫자 요청이 왔습니다.");
        Map<String, Long> res = new HashMap<>();
        res.put("totalActivityNums", userAdminActivityService.getActivityNumByAuthority(userId));
        return res;
    }


}
