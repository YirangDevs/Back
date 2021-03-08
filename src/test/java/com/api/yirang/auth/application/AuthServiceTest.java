package com.api.yirang.auth.application;

import com.api.yirang.auth.application.advancedService.AuthService;
import com.api.yirang.auth.application.basicService.AdminService;
import com.api.yirang.auth.application.basicService.VolunteerBasicService;
import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.auth.domain.user.exceptions.AlreadyExistedAdmin;
import com.api.yirang.auth.domain.user.exceptions.AlreadyExistedVolunteer;
import com.api.yirang.auth.domain.user.model.Admin;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.domain.user.model.Volunteer;
import com.api.yirang.auth.generator.UserGenerator;
import com.api.yirang.auth.presentation.dto.SignInRequestDto;
import com.api.yirang.auth.support.type.Authority;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class AuthServiceTest {

    // test service
    @Autowired
    AuthService authService;

    // DB service
    @Autowired
    VolunteerBasicService volunteerBasicService;
    @Autowired
    AdminService adminService;

    @Autowired
    UserService userService;

    private Admin saveRandomAdmin(){
        User adminUser = UserGenerator.createRandomUser(Authority.ROLE_ADMIN);
        return adminService.save(adminUser);
    }
    private Volunteer saveRandomVolunteer(){
        User volunteerUser = UserGenerator.createRandomUser(Authority.ROLE_VOLUNTEER);
        return volunteerBasicService.save(volunteerUser);
    }


    @Test
    public void 봉사자에서_관리자로_테스트() {

        authService.changeToAdmin(14378L);
        authService.changeToAdmin(17576L);
        authService.changeToAdmin(91621L);

    }

    @Test(expected = AlreadyExistedAdmin.class)
    public void 관리자에서_관리자로_테스트(){
        Admin admin = saveRandomAdmin();

        assertThat(adminService.isExistedByUserId(admin.getUser().getUserId())).isTrue();

        authService.changeToAdmin(admin.getUser().getUserId());
    }

    @Test(expected = AlreadyExistedVolunteer.class)
    public void 봉사자에서_봉사자로_테스트(){
        Volunteer volunteer = saveRandomVolunteer();
        assertThat(volunteerBasicService.existVolunteerByUserId(volunteer.getUser().getUserId())).isTrue();

        authService.changeToVolunteer(volunteer.getUser().getUserId());
    }

    @Test
    public void 관리자에서_봉사자로_테스트(){
        Admin admin = saveRandomAdmin();
        assertThat(adminService.isExistedByUserId(admin.getUser().getUserId())).isTrue();

        authService.changeToVolunteer(admin.getUser().getUserId());
    }

}
