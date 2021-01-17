package com.api.yirang.apply.IntegrationTesting;

import com.api.yirang.apply.application.ApplyAdvancedService;
import com.api.yirang.apply.application.ApplyBasicService;
import com.api.yirang.apply.domain.exception.AlreadyExistedApplyException;
import com.api.yirang.apply.domain.exception.ApplyNullException;
import com.api.yirang.apply.domain.model.Apply;
import com.api.yirang.apply.presentation.dto.ApplicantResponseDto;
import com.api.yirang.apply.presentation.dto.ApplyRegisterRequestDto;
import com.api.yirang.apply.presentation.dto.ApplyResponseDto;
import com.api.yirang.auth.application.basicService.AdminService;
import com.api.yirang.auth.application.basicService.VolunteerBasicService;
import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.auth.domain.user.model.Admin;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.domain.user.model.Volunteer;
import com.api.yirang.auth.generator.UserGenerator;
import com.api.yirang.auth.support.type.Authority;
import com.api.yirang.common.generator.EnumGenerator;
import com.api.yirang.notice.generator.ActivityGenerator;
import com.api.yirang.notice.generator.NoticeGenerator;
import com.api.yirang.notices.application.basicService.ActivityBasicService;
import com.api.yirang.notices.application.basicService.NoticeBasicService;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.notices.domain.notice.model.Notice;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by JeongminYoo on 2021/1/16
 * Work with Yirang
 * Email: likemin0142@gmail.com
 * Blog: http://Beewoom.github.io
 * Github: http://github.com/Biewoom
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ApplyAdvancedServiceIntegrationTest {

    // Test 하고 싶은 서비스
    @Autowired
    ApplyAdvancedService applyAdvancedService;

    // Helper DI
    @Autowired
    UserService userService;
    @Autowired
    AdminService adminService;
    @Autowired
    VolunteerBasicService volunteerBasicService;
    @Autowired
    ActivityBasicService activityBasicService;
    @Autowired
    NoticeBasicService noticeBasicService;

    // Check Helper DI
    @Autowired
    ApplyBasicService applyBasicService;

    private Admin admin;
    private Volunteer volunteer;
    private Activity activity;
    private Notice notice;

    private ApplyRegisterRequestDto applyRegisterRequestDto;

    private Admin saveRandomAdmin(){
        User adminUser = UserGenerator.createRandomUser(Authority.ROLE_ADMIN);
        admin = adminService.save(adminUser);
        return admin;
    }
    private Volunteer saveRandomVolunteer(){
        User volunteerUser = UserGenerator.createRandomUser(Authority.ROLE_VOLUNTEER);
        volunteer = volunteerBasicService.save(volunteerUser);
        return volunteer;
    }

    private Activity saveRandomActivity(){
        Activity randomActivity = ActivityGenerator.createRandomActivity();
        activity = activityBasicService.save(randomActivity);
        return activity;
    }
    private Notice saveRandomNotice(Admin admin, Activity activity){
        notice = noticeBasicService.save(NoticeGenerator.createRandomNotice(admin, activity));
        return notice;
    }


    // 하기 전에 해야할 것
    @Before
    public void setUp(){
        // Admin 만들기
        saveRandomAdmin();
        // Volunteer 만들기
        saveRandomVolunteer();
        // Activity 만들기
        saveRandomActivity();
        // Notice 만들기
        saveRandomNotice(admin, activity);
        // Request 만들기
        applyRegisterRequestDto = ApplyRegisterRequestDto.builder()
                                                         .noticeId(notice.getNoticeId())
                                                         .serviceType(EnumGenerator.generateRandomServiceType())
                                                         .build();
    }

    @After
    public void tearDown(){
        applyBasicService.deleteAll();
        noticeBasicService.deleteAll();
        adminService.deleteAll();
        volunteerBasicService.deleteAll();
        activityBasicService.deleteAll();
    }

    // ApplyToNotice에 대한 TESTs
    @Test
    public void 정상적인_봉사_신청하기(){

        System.out.println("Volunteer: " + volunteer);
        System.out.println("ApplyRegisterRequestDto: " + applyRegisterRequestDto);
        System.out.println("Activity: " + activity);
        System.out.println("Notice: " + notice);
        // 신청한 사람
        Long userId = volunteer.getUser().getUserId();
        applyAdvancedService.applyToNotice(userId, applyRegisterRequestDto);

        // 정상적으로 Apply로 저장이 되었는지?
        assertThat(applyBasicService.existApplyByVolunteerAndActivity(volunteer, activity)).isTrue();
        System.out.println("ExistApplyByVolunteerAndActivity: " + applyBasicService.existApplyByVolunteerAndActivity(volunteer, activity));
        // 다시 Activity 찾기
        Activity newActivity = activityBasicService.findActivityByActivityId(activity.getActivityId());
        System.out.println("newActivity: " + newActivity);
        // 정상적으로 Activity에 숫자가 올라갔는 지?
        assertThat(newActivity.getNoa()).isEqualTo(Long.valueOf(1));
    }

    @Test(expected = AlreadyExistedApplyException.class)
    public void 중복되는_봉사_신청하기(){
        Long userId = volunteer.getUser().getUserId();
        applyAdvancedService.applyToNotice(userId, applyRegisterRequestDto);

        // 또 신청하려고 할 때 에러가 나야함
        System.out.println("I'm trying to second apply");
        applyAdvancedService.applyToNotice(userId, applyRegisterRequestDto);
    }

    // 다른 Notice지만 같은 Acitivty을 지원할 때
    @Test(expected = AlreadyExistedApplyException.class)
    public void 다른_Notice_같은_Activity_지원하기(){
        Long userId = volunteer.getUser().getUserId();

        // Notices 만들기
        Notice notice1 = saveRandomNotice(admin, activity);
        Notice notice2 = saveRandomNotice(admin, activity);

        //
        // 지원하기
        ApplyRegisterRequestDto applyRegisterRequestDto1 = ApplyRegisterRequestDto.builder()
                                                                                 .noticeId(notice1.getNoticeId())
                                                                                 .serviceType(EnumGenerator.generateRandomServiceType())
                                                                                 .build();
        ApplyRegisterRequestDto applyRegisterRequestDto2 = ApplyRegisterRequestDto.builder()
                                                                                 .noticeId(notice2.getNoticeId())
                                                                                 .serviceType(EnumGenerator.generateRandomServiceType())
                                                                                 .build();


        applyAdvancedService.applyToNotice(userId, applyRegisterRequestDto1);

        Activity newActivity = activityBasicService.findActivityByActivityId(activity.getActivityId());
        assertThat(newActivity.getNoa()).isEqualTo(Long.valueOf(1));

        System.out.println("Trying Other notice But Same Activity");
        applyAdvancedService.applyToNotice(userId, applyRegisterRequestDto2);
    }

    // check Applicable
    @Test
    public void 봉사하기_전_봉사_가능한지_테스트(){
        Long userId = volunteer.getUser().getUserId();
        Long noticeId = notice.getNoticeId();

        Boolean res1 = applyAdvancedService.checkApplicableNotice(noticeId, userId);
        assertThat(res1).isTrue();

        // 신청한 이 후
        applyAdvancedService.applyToNotice(userId, applyRegisterRequestDto);
        Boolean res2 = applyAdvancedService.checkApplicableNotice(noticeId, userId);
        assertThat(res2).isFalse();
    }

    @Test
    public void 다른_notice_같은_Activity인_경우_한번만_신청_가능(){
        Long userId = volunteer.getUser().getUserId();

        Notice notice1 = saveRandomNotice(admin, activity);
        Notice notice2 = saveRandomNotice(admin, activity);

        Boolean res1 = applyAdvancedService.checkApplicableNotice(notice1.getNoticeId(), userId);
        Boolean res2 = applyAdvancedService.checkApplicableNotice(notice2.getNoticeId(), userId);
        System.out.println("res1: " + res1);
        System.out.println("res2: " + res2);

        assertThat(res1).isTrue();
        assertThat(res2).isTrue();

        // 신청하기
        ApplyRegisterRequestDto applyRegisterRequestDto1 = ApplyRegisterRequestDto.builder()
                                                                                 .noticeId(notice1.getNoticeId())
                                                                                 .serviceType(EnumGenerator.generateRandomServiceType())
                                                                                 .build();
        applyAdvancedService.applyToNotice(userId, applyRegisterRequestDto1);

        // 신청한 후에는 둘다 false
        Boolean res3 = applyAdvancedService.checkApplicableNotice(notice1.getNoticeId(), userId);
        Boolean res4 = applyAdvancedService.checkApplicableNotice(notice2.getNoticeId(), userId);
        System.out.println("res3: " + res3);
        System.out.println("res4: " + res4);

        assertThat(res3).isFalse();
        assertThat(res4).isFalse();
    }

    // getApplicantsNumsFromNoticeId
    @Test
    public void 봉사에_몇_명이_지원했는지(){
        // Volunteer 더 만들기
        Volunteer volunteer1 = saveRandomVolunteer();
        Volunteer volunteer2 = saveRandomVolunteer();
        Volunteer volunteer3 = saveRandomVolunteer();

        Long noticeId = notice.getNoticeId();

        // 한 봉사에 3명이 지원하기
        applyAdvancedService.applyToNotice(volunteer1.getUser().getUserId(), applyRegisterRequestDto);
        applyAdvancedService.applyToNotice(volunteer2.getUser().getUserId(), applyRegisterRequestDto);
        applyAdvancedService.applyToNotice(volunteer3.getUser().getUserId(), applyRegisterRequestDto);

        Long res = applyAdvancedService.getApplicantsNumsFromNoticeId(noticeId);
        System.out.println("result: " + res);
        assertThat(res).isEqualTo(3);
    }

    // getApplicantsFromNoticeId
    @Test
    public void Applicants들의_정보주기(){
        Volunteer volunteer1 = saveRandomVolunteer();
        Volunteer volunteer2 = saveRandomVolunteer();
        Volunteer volunteer3 = saveRandomVolunteer();
        Volunteer volunteer4 = saveRandomVolunteer();
        Volunteer volunteer5 = saveRandomVolunteer();

        Long noticeId = notice.getNoticeId();

        // 한 봉사에 5명이 지원하기
        applyAdvancedService.applyToNotice(volunteer1.getUser().getUserId(), applyRegisterRequestDto);
        applyAdvancedService.applyToNotice(volunteer2.getUser().getUserId(), applyRegisterRequestDto);
        applyAdvancedService.applyToNotice(volunteer3.getUser().getUserId(), applyRegisterRequestDto);
        applyAdvancedService.applyToNotice(volunteer4.getUser().getUserId(), applyRegisterRequestDto);
        applyAdvancedService.applyToNotice(volunteer5.getUser().getUserId(), applyRegisterRequestDto);

        Collection<ApplicantResponseDto> applicantResponseDtos = applyAdvancedService.getApplicantsFromNoticeId(noticeId);
        System.out.println("applicantResponseDtos: " + applicantResponseDtos);
        assertThat(applyAdvancedService.getApplicantsNumsFromNoticeId(noticeId)).isEqualTo(5);
    }
    // GetMyApplies
    @Test
    public void 내가_지원했던_Apply들_보기(){

        Long userId = volunteer.getUser().getUserId();

        // 여러 Activity 만들기
        Activity activity1 = saveRandomActivity();
        Activity activity2 = saveRandomActivity();
        Activity activity3 = saveRandomActivity();

        // 연계되는 Notice 만들기
        Notice notice1 = saveRandomNotice(admin, activity1);
        Notice notice2 = saveRandomNotice(admin, activity2);
        Notice notice3 = saveRandomNotice(admin, activity3);

        // 다 지원하기
        ApplyRegisterRequestDto applyRegisterRequestDto1 = ApplyRegisterRequestDto.builder()
                                                                                  .noticeId(notice1.getNoticeId())
                                                                                  .serviceType(EnumGenerator.generateRandomServiceType())
                                                                                  .build();
        ApplyRegisterRequestDto applyRegisterRequestDto2 = ApplyRegisterRequestDto.builder()
                                                                                  .noticeId(notice2.getNoticeId())
                                                                                  .serviceType(EnumGenerator.generateRandomServiceType())
                                                                                  .build();
        ApplyRegisterRequestDto applyRegisterRequestDto3 = ApplyRegisterRequestDto.builder()
                                                                                  .noticeId(notice3.getNoticeId())
                                                                                  .serviceType(EnumGenerator.generateRandomServiceType())
                                                                                  .build();
        applyAdvancedService.applyToNotice(userId, applyRegisterRequestDto1);
        applyAdvancedService.applyToNotice(userId, applyRegisterRequestDto2);
        applyAdvancedService.applyToNotice(userId, applyRegisterRequestDto3);

        // 신청했던 List
        Collection<ApplyResponseDto> applyResponseDtos = applyAdvancedService.getMyApplies(userId);
        // 갯수는 3개
        System.out.println("ApplyResponseDtos: " + applyResponseDtos);
        assertThat(applyResponseDtos.size()).isEqualTo(3);
    }


    // cancelApply Tests

    @Test
    public void Apply_신청했다가_취소하기(){
        Long userId = volunteer.getUser().getUserId();

        Long applyId = applyAdvancedService.applyToNotice(userId, applyRegisterRequestDto);

        System.out.println("신청했습니다.");

        assertThat(applyBasicService.existApplyByVolunteerAndActivity(volunteer, activity)).isTrue();
        // 다시 Activity 찾기
        Activity newActivity = activityBasicService.findActivityByActivityId(activity.getActivityId());
        // 정상적으로 Activity에 숫자가 올라갔는 지?
        assertThat(newActivity.getNoa()).isEqualTo(Long.valueOf(1));

        // 취소하기
        applyAdvancedService.cancelApply(applyId);
        assertThat(applyBasicService.existApplyByVolunteerAndActivity(volunteer, activity)).isFalse();

        Activity afterActivity = activityBasicService.findActivityByActivityId(activity.getActivityId());

        // 정상적으로 Activity에 숫자가 내려갔는지?
        assertThat(afterActivity.getNoa()).isEqualTo(Long.valueOf(0));

    }

    @Test(expected = ApplyNullException.class)
    public void 신청한_적_없는_apply를_취소하기(){
        applyAdvancedService.cancelApply(Long.valueOf(5));
    }


    @Test(expected = ApplyNullException.class)
    public void 두_번_삭제하려고할때(){
        Long userId = volunteer.getUser().getUserId();

        Long applyId = applyAdvancedService.applyToNotice(userId, applyRegisterRequestDto);

        System.out.println("신청했습니다.");

        assertThat(applyBasicService.existApplyByVolunteerAndActivity(volunteer, activity)).isTrue();
        // 다시 Activity 찾기
        Activity newActivity = activityBasicService.findActivityByActivityId(activity.getActivityId());
        // 정상적으로 Activity에 숫자가 올라갔는 지?
        assertThat(newActivity.getNoa()).isEqualTo(Long.valueOf(1));

        // 두 번 취소하기
        applyAdvancedService.cancelApply(applyId);
        applyAdvancedService.cancelApply(applyId);
    }

    @Test
    public void apply_취소한_후에는_다시_Applicable_가능함(){
        Long userId = volunteer.getUser().getUserId();

        Long applyId = applyAdvancedService.applyToNotice(userId, applyRegisterRequestDto);

        System.out.println("봉사 활동을 신청했습니다.");

        assertThat(applyAdvancedService.checkApplicableNotice(notice.getNoticeId(), userId)).isFalse();
        // 다시 Activity 찾기
        // 정상적으로 Activity에 숫자가 올라갔는 지?

        System.out.println("봉사 활동을 취소했습니다.");
        // 취소하기
        applyAdvancedService.cancelApply(applyId);
        assertThat(applyAdvancedService.checkApplicableNotice(notice.getNoticeId(), userId)).isTrue();


    }



}
