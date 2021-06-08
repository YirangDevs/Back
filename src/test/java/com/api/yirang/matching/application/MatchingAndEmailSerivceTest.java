package com.api.yirang.matching.application;

import com.api.yirang.apply.domain.model.Apply;
import com.api.yirang.apply.generator.ApplyGenerator;
import com.api.yirang.apply.repository.persistence.maria.ApplyDao;
import com.api.yirang.auth.domain.user.model.Volunteer;
import com.api.yirang.auth.generator.VolunteerGenerator;
import com.api.yirang.auth.repository.persistence.maria.VolunteerDao;
import com.api.yirang.common.support.type.Region;
import com.api.yirang.common.support.type.Sex;
import com.api.yirang.email.application.EmailAdvancedService;
import com.api.yirang.matching.repository.maria.MatchingRepository;
import com.api.yirang.notice.generator.ActivityGenerator;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.notices.repository.persistence.maria.ActivityDao;
import com.api.yirang.senior.generator.SeniorGenerator;
import com.api.yirang.senior.generator.VolunteerServiceGenerator;
import com.api.yirang.seniors.domain.senior.model.Senior;
import com.api.yirang.seniors.domain.volunteerService.model.VolunteerService;
import com.api.yirang.seniors.repository.persistence.maria.SeniorDao;
import com.api.yirang.seniors.repository.persistence.maria.VolunteerServiceDao;
import com.api.yirang.seniors.support.custom.ServiceType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class MatchingAndEmailSerivceTest {

    @Autowired
    ActivityDao activityDao;

    @Autowired
    ApplyDao applyDao;

    @Autowired
    VolunteerDao volunteerDao;

    @Autowired
    SeniorDao seniorDao;

    @Autowired
    MatchingRepository matchingRepository;

    @Autowired
    EmailAdvancedService emailAdvancedService;

    @Autowired
    VolunteerServiceDao volunteerServiceDao;

    @Autowired
    MatchingLogicService matchingLogicService;

    @Autowired
    MatchingService matchingService;

    Activity activity;

    private void makeVolunteerAndApplyActivity(Sex sex, ServiceType serviceType){
        Volunteer volunteer = VolunteerGenerator.createAndRandomVolunteer(volunteerDao, sex, "likemin5517@naver.com");
        System.out.println("Volunteer: " + volunteer);
        Apply apply = ApplyGenerator.createAndStoreRandomApply(applyDao, volunteer, activity, serviceType);
        System.out.println("Apply: " + apply);

    }

    private void makeSeniorAndServiceActivity(Sex sex, ServiceType serviceType){
        Senior senior = SeniorGenerator.createAndSaveRandomSenior(seniorDao, sex, Region.DALSEO_DISTRICT, "이상한 ");
        System.out.println("Senior: " + senior);
        VolunteerService volunteerService = VolunteerServiceGenerator.createAndStoreRandomVolunteerService(volunteerServiceDao,
                                                                                                           senior, activity,
                                                                                                           serviceType);
        System.out.println("VolunteerService: " + volunteerService);
    }

    @Before
    public void startUp(){
        activity = ActivityGenerator.createAndStoreRandomActivity(activityDao);
    }

    @After
    public void tearDown(){
        matchingRepository.deleteAll();
        applyDao.deleteAll();
        volunteerDao.deleteAll();
        volunteerServiceDao.deleteAll();
        seniorDao.deleteAll();
    }

    @Test
    public void 간단_테스트(){
        // 여자-노력 봉사자: 1
        // 여자-노력-피봉사자: 1
        makeVolunteerAndApplyActivity(Sex.SEX_FEMALE, ServiceType.SERVICE_WORK);
        makeSeniorAndServiceActivity(Sex.SEX_FEMALE, ServiceType.SERVICE_WORK);
        matchingLogicService.executeMatchingSteps(activity);

        System.out.println(matchingService.findUnMatchingByActivityId(activity.getActivityId()) );
        System.out.println(matchingService.findMatchingByActivityId(activity.getActivityId()) );

        emailAdvancedService.sendEmailToVolunteerAboutTomorrowActivity(activity);
    }

    @Test
    public void 성공2_실패2_테스트(){
        // 남자-말벗-봉사자: 2, 남자-노력-봉사자: 2
        // 남자-노력-피봉사자: 1
        makeVolunteerAndApplyActivity(Sex.SEX_MALE, ServiceType.SERVICE_TALK);
        makeVolunteerAndApplyActivity(Sex.SEX_MALE, ServiceType.SERVICE_TALK);

        makeVolunteerAndApplyActivity(Sex.SEX_MALE, ServiceType.SERVICE_WORK);
        makeVolunteerAndApplyActivity(Sex.SEX_MALE, ServiceType.SERVICE_WORK);

        makeSeniorAndServiceActivity(Sex.SEX_MALE, ServiceType.SERVICE_WORK);

        matchingLogicService.executeMatchingSteps(activity);

        System.out.println(matchingService.findUnMatchingByActivityId(activity.getActivityId()) );
        System.out.println(matchingService.findMatchingByActivityId(activity.getActivityId()) );
        emailAdvancedService.sendEmailToVolunteerAboutTomorrowActivity(activity);

    }
}
