package com.api.yirang.notices.presentation.controller;


import com.api.yirang.email.application.EmailAdvancedService;
import com.api.yirang.notices.application.advancedService.NoticeActivityService;
import com.api.yirang.notices.domain.notice.model.Notice;
import com.api.yirang.notices.presentation.dto.NoticeOneResponseDto;
import com.api.yirang.notices.presentation.dto.NoticeRegisterRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/apis/manage/notices")
public class NoticeController {

    // DI 할 필드
    private final NoticeActivityService noticeActivityService;
    private final EmailAdvancedService emailAdvancedService;

    /** POST methods **/
    // 공고 등록
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerNewNotice(@RequestHeader("Authorization") String header,
                                  @RequestBody @Valid NoticeRegisterRequestDto noticeRequestDto){
        System.out.println("[NoticeController] 공고 등록 요청이 왔습니다.");
        System.out.println("[NoticeController] 요청: " + noticeRequestDto);
        Notice notice = noticeActivityService.registerNew(header, noticeRequestDto);
        emailAdvancedService.sendEmailToVolunteerAboutRecommendedActivity(notice);
    }
    // 긴급 공고 등록
    @PostMapping(value = "/{notice_id}/urgent", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUrgentNotice(@RequestHeader("Authorization") String header,
                                     @PathVariable("notice_id") Long noticeId,
                                     @RequestBody Map<String, String> param){
        System.out.println("[NoticeController] 공고 긴급 등록 요청이 왔습니다.");
        String title = param.get("title");
        Notice notice = noticeActivityService.registerUrgent(header, noticeId, title);
        emailAdvancedService.sendEmailToVolunteerAboutRecommendedActivity(notice);
    }

    /** Get methods **/

    // 공고 paging 조회
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Collection> getPageNotices(@Param("page") @Min(value = 0) Integer page){
        System.out.println("[NoticeController] 공고 페이지 조회 요청이 왔습니다.");
        System.out.println("[NoticeController] PageNums: " + page);
        Map<String, Collection> res = new HashMap<>();
        res.put("notices", noticeActivityService.findNoticesByPage(page));
        return res;
    }

    // 공고 하나 조회
    @GetMapping(value = "/{notice_id}",produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public NoticeOneResponseDto getOneNotice(@PathVariable("notice_id") Long noticeId){
        System.out.println("[NoticeController] 공고 조회 요청이 왔습니다.");
        return noticeActivityService.getOneNoticeById(noticeId);
    }

    // 공고 갯수 조회
    @GetMapping(value = "/nums", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Long> getNumsOfNotices(){
        System.out.println("[NoticeController] 공고 숫자 요청이 왔습니다.");
        Map<String, Long> res = new HashMap<>();
        res.put("totalNoticeNums", noticeActivityService.findNumsOfNotices());
        return res;
    }

    /** PUT methods **/
    // 공고 업데이트
    @PutMapping(value = "/{notice_id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void updateOneNotice(@RequestHeader("Authorization") String header,
                                @PathVariable("notice_id") Long noticeId,
                                @RequestBody @Valid NoticeRegisterRequestDto noticeRegisterRequestDto){
        System.out.println("[NoticeController] 공고 업데이트 요청이 왔습니다.");
        noticeActivityService.updateOneNotice(header, noticeId, noticeRegisterRequestDto);
    }

    /** Delete methods **/

    // 공고 삭제
    @DeleteMapping(value = "/{notice_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOneNotice(@PathVariable("notice_id") Long noticeId){
        System.out.println("[NoticeController] 공고 삭제 요청이 왔습니다.");
        noticeActivityService.deleteOneNotice(noticeId);
        System.out.println("[NoticeController] 공고 삭제를 했습니다.");

    }
    // 공고 강력 삭제(Force)
    @DeleteMapping(value = "/force/{notice_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOneNoticeWithForce(@PathVariable("notice_id") Long noticeId){
        System.out.println("[NoticeController] 공고 강력 삭제 요청이 왔습니다.");
        noticeActivityService.deleteOneNoticeWithForce(noticeId);
        System.out.println("[NoticeController] 공고 삭제 요청을 했습니다.");

    }

}
