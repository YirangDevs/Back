package com.api.yirang.apply.presentation.controller;

import com.api.yirang.apply.application.ApplyAdvancedService;
import com.api.yirang.apply.presentation.dto.ApplyRegisterRequestDto;
import com.api.yirang.auth.domain.jwt.components.JwtParser;
import com.api.yirang.auth.support.utils.ParsingHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by JeongminYoo on 2020/12/29
 * Work with Yirang
 * Email: likemin0142@gmail.com
 * Blog: http://Beewoom.github.io
 * Github: http://github.com/Biewoom
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/apis/apply")
public class ApplyController {

    // DI 할 Field
    private final ApplyAdvancedService applyAdvancedService;
    private final JwtParser jwtParser;

    // GET
    /**
     * 관리자는 해당하는 게시글에 신청한 봉사자의 정보를 얻는다.
     */
    @GetMapping(value = "/notices/{notice_id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Collection> getVolunteersFromNotice(@PathVariable("notice_id") Long noticeId){
        System.out.println("[ApplyController] 봉사자 신청목록 조회 요청이 왔습니다.");
        Map<String, Collection> res = new HashMap<>();
        res.put("volunteers", applyAdvancedService.getApplicantsFromNoticeId(noticeId));
        return res;
    }
    /**
     * 해당하는 게시글에 신청한 사람의 수를 구한다.
     */
    @GetMapping(value= "/applicant-nums/notices/{notice_id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Long> getApplicantNumsFromNotice(@PathVariable("notice_id") Long noticeId){
        System.out.println("[ApplyController] 봉사자 신청한 사람의 수를 조회하는 요청이 왔습니다.");
        Map<String, Long> res = new HashMap<>();
        res.put("numsOfVolunteers", applyAdvancedService.getApplicantsNumsFromNoticeId(noticeId));
        return res;
    }

    /**
     * 봉사자는 해당하는 게시글을 신청할 수 있는 지 아닌 지 정보를 얻는다.
     */
    @GetMapping(value = "/apply-check/notices/{notice_id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Boolean> checkApplicableFromNotice(@PathVariable("notice_id") Long noticeId,
                                                         @RequestHeader("Authorization") String header){
        System.out.println("[ApplyController] 봉사 신청을 할 수 있는 지 체크합니다.");
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));

        Map<String, Boolean> res = new HashMap<>();
        res.put("Applicable", applyAdvancedService.checkApplicableNotice(noticeId, userId));
        return res;
    }

    /**
     *  봉사자는 자신이 신청한 봉사들을 본다.
     */
    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Collection> getMyApplicants(@RequestHeader("Authorization") String header){
        System.out.println("[ApplyController] 자신의 봉사 신청 목록들을 봅니다.");
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        Map<String, Collection> res = new HashMap<>();
        res.put("Applicants", applyAdvancedService.getMyApplies(userId));
        return res;
    }

    // POST
    /**
     * 봉사자는 Notice ID를 입력해서 해당 게시글에 해당하는 봉사활동을 신청한다.
     */
    @PostMapping(value = "/notices", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerApplyNotice(@RequestBody @Valid ApplyRegisterRequestDto applyRegisterRequestDto,
                                    @RequestHeader("Authorization") String header){
        System.out.println("[ApplyController] 봉사활동 신청 요청이 왔습니다.");
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        applyAdvancedService.applyToNotice(userId, applyRegisterRequestDto);
    }

    // DELETE
    /**
     * 봉사자나 관리자는 해당 Apply 신청을 취소 할 수 있다.
     */
    @DeleteMapping(value = "/{apply_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelApply(@PathVariable("apply_id") Long applyId){
        System.out.println("[ApplyController] 봉사 신청 취소 요청이 왔습니다." );
        applyAdvancedService.cancelApply(applyId);
    }
}
