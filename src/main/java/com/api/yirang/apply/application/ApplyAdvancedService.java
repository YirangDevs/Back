package com.api.yirang.apply.application;

import com.api.yirang.apply.domain.converter.ApplyConverter;
import com.api.yirang.apply.domain.model.Apply;
import com.api.yirang.apply.presentation.dto.ApplyRegisterRequestDto;
import com.api.yirang.auth.application.basicService.VolunteerBasicService;
import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.auth.domain.user.model.Volunteer;
import com.api.yirang.notices.application.advancedService.NoticeActivityService;
import com.api.yirang.notices.application.basicService.NoticeBasicService;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.seniors.support.custom.ServiceType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * Created by JeongminYoo on 2020/12/30
 * Work with Yirang
 * Email: likemin0142@gmail.com
 * Blog: http://Beewoom.github.io
 * Github: http://github.com/Biewoom
 */

@Service
@RequiredArgsConstructor
@Transactional
public class ApplyAdvancedService {

    // DI basic services [In this Feature]
    private final ApplyBasicService applyBasicService;

    // DI other service [In other Features]
    private final UserService userService;
    private final NoticeActivityService noticeActivityService;

    // DI other basic Service
    private final NoticeBasicService noticeBasicService;
    private final VolunteerBasicService volunteerBasicService;

    /** TO-DO
     * 봉사자들 신청 목록에는 어떤 것이 들어갈까요?
     **/
    public Collection getVolunteersFromNoticeId(Long noticeId) {
        return null;
    }


    /**
     * @param userId 봉사자의 아이디
     * @return Applicants에는 어떤 것이 들어 가야 할까요?
     */
    public Collection getMyApplicants(Long userId) {
        return null;
    }

    //
    public void applyToNotice(Long userId, ApplyRegisterRequestDto applyRegisterRequestDto) {
        // serviceType 이랑 noticeId 추적 하기
        ServiceType serviceType = applyRegisterRequestDto.getServiceType();
        Long noticeId = applyRegisterRequestDto.getNoticeId();

        // Volunteer을 찾음
        Volunteer volunteer = volunteerBasicService.findVolunteerByUserId(userId);
        // Activity를 찾음
        Activity activity = noticeBasicService.findActivityNoticeId(noticeId);

        // Apply 만들기
        Apply apply = ApplyConverter.makeApplyfromInfos(serviceType, volunteer, activity);
        // Apply 저장하기
        applyBasicService.save(apply);
    }


}
