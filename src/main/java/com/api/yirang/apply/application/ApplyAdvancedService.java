package com.api.yirang.apply.application;

import com.api.yirang.apply.domain.converter.ApplyConverter;
import com.api.yirang.apply.domain.model.Apply;
import com.api.yirang.apply.presentation.converter.ApplyDtoConverter;
import com.api.yirang.apply.presentation.dto.ApplicantResponseDto;
import com.api.yirang.apply.presentation.dto.ApplyRegisterRequestDto;
import com.api.yirang.apply.presentation.dto.ApplyResponseDto;
import com.api.yirang.auth.application.basicService.VolunteerBasicService;
import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.auth.domain.user.model.Volunteer;
import com.api.yirang.notices.application.advancedService.NoticeActivityService;
import com.api.yirang.notices.application.basicService.ActivityBasicService;
import com.api.yirang.notices.application.basicService.NoticeBasicService;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.seniors.support.custom.ServiceType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
    private final ActivityBasicService activityBasicService;
    private final VolunteerBasicService volunteerBasicService;

    /** TO-DO
     * 봉사자들 Notice ID 에 해당하는 Applicant 들의 정보를 주기
     **/
    public Collection<ApplicantResponseDto> getApplicantsFromNoticeId(Long noticeId) {
        // 0. noticeId에 해당하는 Activity를 찾기
        Activity activity = noticeBasicService.findActivityNoticeId(noticeId);
        // 1. Notice ID에 신청한 Apply들을 불러오기
        Collection<Apply> applies = applyBasicService.getAppliesFromActivity(activity);
        // 2. 각 Apply에 해당하는 Volunteer들을 불러오기
        Collection<ApplicantResponseDto> applicantResponseDtos = applies.stream()
                                                                        .map(e -> ApplyDtoConverter.makeApplicantResponseDto(e.getServiceType(), e.getVolunteer().getUser()))
                                                                        .collect(Collectors.toList());
         return applicantResponseDtos;
    }

    public Long getApplicantsNumsFromNoticeId(Long noticeId) {
        // 0. noticeId에 해당하는 Activity를 찾기
        Activity activity = noticeBasicService.findActivityNoticeId(noticeId);
        // 1. Notice ID에 신청한 Apply들을 불러오기
        Collection<Apply> applies = applyBasicService.getAppliesFromActivity(activity);

        // 2. 몇 개인지 세아리고 Return 하기
        Long res = Long.valueOf(applies.size());
        return res;
    }

    public List<Volunteer> getVolunteersFromActivityId(Activity activity){
        // 0. apply에 해당하는 Activity 찾기
        Collection<Apply> applies = applyBasicService.getAppliesFromActivity(activity);

        return applies.stream()
                      .map(Apply::getVolunteer)
                      .collect(Collectors.toList());
    }
    public List<Volunteer> getWorkVolunteersFromActivityId(Activity activity){
        // 0. apply에 해당하는 Activity 찾기
        Collection<Apply> applies = applyBasicService.getAppliesFromActivity(activity, ServiceType.SERVICE_WORK);

        return applies.stream()
                      .map(Apply::getVolunteer)
                      .collect(Collectors.toList());
    }
    public List<Volunteer> getTalkVolunteersFromActivityId(Activity activity){
        // 0. apply에 해당하는 Activity 찾기
        Collection<Apply> applies = applyBasicService.getAppliesFromActivity(activity, ServiceType.SERVICE_TALK);

        return applies.stream()
                      .map(Apply::getVolunteer)
                      .collect(Collectors.toList());
    }


    /**
     * @param userId 봉사자의 아이디
     * 최근 Applicants 순으로 준다.
     * @return Collection<ApplyResponseDto>
     */
    public Collection<ApplyResponseDto> getMyApplies(Long userId) {

        // 0. userId로 Volunteer 찾기
        Volunteer volunteer = volunteerBasicService.findVolunteerByUserId(userId);
        // 1. Volunteer에 해당하는 applies 들 찾기
        Collection<Apply> applies = applyBasicService.getAppliesFromVolunteer(volunteer);
        // 2. 각 Applies로 ApplyResponseDTO 만들기
        Collection<ApplyResponseDto> applyResponseDtos = applies.stream().map(apply -> ApplyDtoConverter.makeApplyResponseFromApply(apply))
                                                                .collect(Collectors.toList());
        return applyResponseDtos;
    }

    // Check
    public Boolean checkApplicableNotice(Long noticeId, Long userId) {
        // 0. userId로 Volunteer 찾기
        Volunteer volunteer = volunteerBasicService.findVolunteerByUserId(userId);

        // 1. noticeId로 activity를 찾기
        Activity activity = noticeBasicService.findActivityNoticeId(noticeId);

        // 2. userId와 noticeId로 apply가 있는 지 없는 지 찾기.
        Boolean existApply = applyBasicService.existApplyByVolunteerAndActivity(volunteer, activity);

        // 3. return 값은 Apply 존재 여부의 반대의 값
        return !existApply;
    }


    public Long applyToNotice(Long userId, ApplyRegisterRequestDto applyRegisterRequestDto) {
        // serviceType 이랑 noticeId 추적 하기
        ServiceType serviceType = applyRegisterRequestDto.getServiceType();
        Long noticeId = applyRegisterRequestDto.getNoticeId();

        // Volunteer을 찾음
        Volunteer volunteer = volunteerBasicService.findVolunteerByUserId(userId);
        // Activity를 찾음
        Activity activity = noticeBasicService.findActivityNoticeId(noticeId);

        // 중복 되는 Apply가 있는지 없는 지 체크
        applyBasicService.checkApplyByVolunteerAndActivity(volunteer, activity);

        // Apply 만들기
        Apply apply = ApplyConverter.makeApplyfromInfos(serviceType, volunteer, activity);
        // Apply 저장하기
        Apply beSavedApply = applyBasicService.save(apply);
        // Activity에 신청하는 사람 수 올리기.
        activityBasicService.addNumberOfApplicants(activity);

        return beSavedApply.getApplyId();
    }

    // Apply를 취소하는 함수
    public void cancelApply(Long applyId) {
        // 0. applyId에 해당하는 Apply가 있는 지 확인
        Apply apply = applyBasicService.findApplyByApplyId(applyId);
        // 1. 찾은 apply를 통해 activity를 찾기
        Activity activity = apply.getActivity();
        // 2. apply 기록 삭제
        applyBasicService.delete(apply);
        // 3. activity에 신청하는 사람 수 줄이기.
        activityBasicService.subtractNumberOfApplicants(activity);
    }

}
