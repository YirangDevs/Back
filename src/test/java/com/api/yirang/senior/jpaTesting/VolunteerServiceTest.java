package com.api.yirang.senior.jpaTesting;


import com.api.yirang.notice.generator.ActivityGenerator;
import com.api.yirang.notices.application.basicService.ActivityBasicService;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.senior.generator.SeniorGenerator;
import com.api.yirang.senior.generator.VolunteerServiceGenerator;
import com.api.yirang.seniors.application.basicService.SeniorBasicService;
import com.api.yirang.seniors.application.basicService.VolunteerServiceBasicService;
import com.api.yirang.seniors.domain.senior.model.Senior;
import com.api.yirang.seniors.domain.volunteerService.model.VolunteerService;
import com.api.yirang.seniors.support.custom.ServiceType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class VolunteerServiceTest {

    // Test 할 service
    @Autowired
    VolunteerServiceBasicService volunteerServiceBasicService;

    @Autowired
    ActivityBasicService activityBasicService;

    @Autowired
    SeniorBasicService seniorBasicService;

    // variables
    Activity activity = ActivityGenerator.createRandomActivity();
    Senior senior = SeniorGenerator.createRandomSenior();

    @Before
    public void setUp(){
        activityBasicService.save(activity);
        seniorBasicService.save(senior);
    }

    @After
    public void tearDown(){
    }

    @Test
    public void 서비스_등록하고_어떻게_저장되어있는지_보기(){
        VolunteerService volunteerService = VolunteerServiceGenerator.createRandomVolunteerService(senior, activity);
        volunteerServiceBasicService.save(volunteerService);

        Senior senior = volunteerService.getSenior();
        Activity activity = volunteerService.getActivity();

        ServiceType serviceType = volunteerService.getServiceType();
        Long priority = volunteerService.getPriority();
        Long numsOfRequiredVolunteers = volunteerService.getNumsOfRequiredVolunteers();

        VolunteerService foundVolunteerService = volunteerServiceBasicService.findVolunteerServiceByActivityAndSenior(activity, senior);

        System.out.println("FoundVolunteerService: " + foundVolunteerService.toString());
        assertThat(foundVolunteerService.getServiceType()).isEqualTo(serviceType);
        assertThat(foundVolunteerService.getPriority()).isEqualTo(priority);
        assertThat(foundVolunteerService.getNumsOfRequiredVolunteers()).isEqualTo(Long.valueOf(2));
    }
}
