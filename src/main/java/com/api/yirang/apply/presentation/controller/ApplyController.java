package com.api.yirang.apply.presentation.controller;

import com.api.yirang.apply.application.ApplyAdvancedService;
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

    /** [TO-DO]
     * 관리자는 해당하는 게시글에 신청한 봉사자의 정보를 얻는다.
     */
    @GetMapping(value = "/notices/{notice_id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Collection> getVolunteersFromNotice(@PathVariable("notice_id") Long noticeId){
        System.out.println("[ApplyController] 봉사자 신청목록 조회 요청이 왔습니다.");
        // make return
        Map<String, Collection> res = new HashMap<>();
        res.put("Applicants", applyAdvancedService.getVolunteersFromNoticeId(noticeId));
        return res;
    }

    /** [TO-DO]
     *  봉사자는 자신이 신청한 봉사들을 본다.
     */

    // POST

    /** [TO-DO]
     * 봉사자는 Notice ID 를 입력해서 해당 게시글에 해당하는 봉사활동을 신청한다.
     */
    @PostMapping(value = "/notices", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerApplyNotice(@RequestBody @Valid ApplyRegisterRequestDto applyRegisterRequestDto,
                                    @RequestHeader("Authorization") String header){
        System.out.println("[ApplyController] 봉사활동 신청 요청이 왔습니다.");
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));


    }
}
