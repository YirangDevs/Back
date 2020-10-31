package com.api.yirang.notices.presentation.controller;


import com.api.yirang.notices.application.advancedService.NoticeActivityService;
import com.api.yirang.notices.presentation.dto.NoticeNumsDto;
import com.api.yirang.notices.presentation.dto.NoticeOneResponseDto;
import com.api.yirang.notices.presentation.dto.NoticeRegisterRequestDto;
import com.api.yirang.notices.presentation.dto.NoticeResponsesDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/apis/manage/notices")
public class NoticeController {

    // DI 할 필드
    private final NoticeActivityService noticeActivityService;


    // 공고 등록
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerNewNotice(@RequestHeader("Authorization") String header,
                                  @RequestBody @Valid NoticeRegisterRequestDto noticeRequestDto){
        noticeActivityService.registerNew(header, noticeRequestDto);
    }
    // 긴급 공고 등록
    @PostMapping(value = "/{notice_id}/urgent", consumes = )
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUrgentNotice(@RequestHeader("Authorization") String header,
                                     @PathVariable("notice_id") Long noticeId,
                                     @RequestBody @Valid UrgentNoticeRequestDto urgentNoticeRequestDto){
        noticeActivityService.registerUrgent(header,noticeId, urgentNoticeRequestDto);
    }

    // 공고 paging 조회
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public NoticeResponsesDto getPageNotices(@Param("page") Integer pageNum){
        return noticeActivityService.findNoticesByPage(pageNum);
    }

    // 공고 하나 조회
    @GetMapping(value = "/{notice_id}",produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public NoticeOneResponseDto getOneNotice(@PathVariable("notice_id") Long noticeId){
        return noticeActivityService.getOneNoticeById(noticeId);
    }

    // 공고 갯수 조회
    @GetMapping(value = "/nums")
    @ResponseStatus
    public NoticeNumsDto getNumsOfNotices(){
        return noticeActivityService.findNumsOfNotices();
    }

    // 공고 업데이트
    @PutMapping(value = "/{notice_id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void updateOneNotice(@RequestHeader("Authorization") String header,
                                @PathVariable("notice_id") Long noticeId){
        noticeActivityService.updateOneNotice(header, noticeId);
    }

    // 공고 삭제
    @DeleteMapping(value = "/{notice_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOneNotice(@PathVariable("notice_id") Long noticeId){
        noticeActivityService.deleteOneNotice(noticeId);
    }


}
