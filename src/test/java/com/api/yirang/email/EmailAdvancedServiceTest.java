package com.api.yirang.email;


import com.api.yirang.email.application.EmailAdvancedService;
import com.api.yirang.notices.application.basicService.ActivityBasicService;
import com.api.yirang.notices.domain.activity.model.Activity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailAdvancedServiceTest {


    @Autowired
    EmailAdvancedService emailAdvancedService;

    @Autowired
    ActivityBasicService activityBasicService;

    @Test
    public void 매칭_이메일_테스트(){
        Activity activity = activityBasicService.findActivityByActivityId(417L);
        emailAdvancedService.sendEmailToAdminAboutMatching(activity);
    }
    @Test
    public void 유저_탈퇴_테스트(){
        emailAdvancedService.sendEmailToAdminAboutUserWithdraw(25158L);
    }
}
