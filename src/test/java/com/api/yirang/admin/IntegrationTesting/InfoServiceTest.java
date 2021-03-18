package com.api.yirang.admin.IntegrationTesting;

import com.api.yirang.auth.application.basicService.AdminService;
import com.api.yirang.auth.application.basicService.VolunteerBasicService;
import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.auth.domain.user.model.Admin;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.domain.user.model.Volunteer;
import com.api.yirang.auth.generator.UserGenerator;
import com.api.yirang.auth.support.type.Authority;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by JeongminYoo on 2021/1/21
 * Work with Yirang
 * Email: likemin0142@gmail.com
 * Blog: http://Beewoom.github.io
 * Github: http://github.com/Biewoom
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class InfoServiceTest {

    // Test 하고 싶은 서비스
    @Autowired
    UserService userService;

    // DI
    @Autowired
    AdminService adminService;

    @Autowired
    VolunteerBasicService volunteerBasicService;

    private Admin admin;
    private Volunteer volunteer;

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

    @Before
    public void setUp(){
        saveRandomAdmin();
        saveRandomVolunteer();
    }

    @After
    public void tearDown(){
        adminService.deleteAll();
        volunteerBasicService.deleteAll();
    }


    @Test
    public void 봉사자를_찾아서_업데이트_하기(){

    }


}
