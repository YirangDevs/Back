package com.api.yirang.matching;


import com.api.yirang.auth.domain.user.model.Volunteer;
import com.api.yirang.auth.repository.persistence.maria.VolunteerDao;
import com.api.yirang.email.application.EmailAdvancedService;
import com.api.yirang.matching.model.maria.Matching;
import com.api.yirang.matching.repository.maria.MatchingRepository;
import com.api.yirang.notice.generator.ActivityGenerator;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.notices.repository.persistence.maria.ActivityDao;
import com.api.yirang.seniors.domain.senior.model.Senior;
import com.api.yirang.seniors.repository.persistence.maria.SeniorDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest
public class matchingCrudTest {


    @Autowired
    MatchingRepository matchingRepository;

    @Autowired
    SeniorDao seniorDao;

    @Autowired
    ActivityDao activityDao;

    @Autowired
    VolunteerDao volunteerDao;

    @Autowired
    EmailAdvancedService emailAdvancedService;

    @Test
    public void 매칭_보내기(){
        Senior senior = seniorDao.findSeniorByPhone("01099991224").orElseGet(null);
        Activity activity = activityDao.findById(577L).orElseGet(null);
        Volunteer volunteer = volunteerDao.findVolunteerByUserId(25158L).orElseGet(null);

        Matching matching = Matching.builder()
                                    .senior(senior)
                                    .activity(activity)
                                    .volunteer(volunteer)
                                    .build();

        matchingRepository.save(matching);

    }


}
