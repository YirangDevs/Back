package com.api.yirang.admin.IntegrationTesting;

import com.api.yirang.auth.application.basicService.AdminService;
import com.api.yirang.auth.application.basicService.VolunteerBasicService;
import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.auth.domain.user.exceptions.AdminNullException;
import com.api.yirang.auth.domain.user.exceptions.AlreadyExistedAdmin;
import com.api.yirang.auth.domain.user.exceptions.UserNullException;
import com.api.yirang.auth.domain.user.model.Admin;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.domain.user.model.Volunteer;
import com.api.yirang.auth.generator.UserGenerator;
import com.api.yirang.auth.support.type.Authority;
import com.api.yirang.common.support.type.Region;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

    // 실험하고자 하는 Service
    @Autowired
    UserService userService;

    // DB 확인용
    @Autowired
    AdminService adminService;

    @Autowired
    VolunteerBasicService volunteerBasicService;

    // Variable
    long userId = 1468416139; //  현재 Admin이 아닌 User임
    long nonExistedUserId = 1234;


    private Admin saveRandomAdmin(){
        User adminUser = UserGenerator.createRandomUser(Authority.ROLE_ADMIN);
        return adminService.save(adminUser);
    }

    private Admin saveRandomSuperAdmin(){
        User adminUser = UserGenerator.createRandomUser(Authority.ROLE_SUPER_ADMIN);
        return adminService.save(adminUser);
    }

    private Volunteer saveRandomVolunteer(){
        User volunteerUser = UserGenerator.createRandomUser(Authority.ROLE_VOLUNTEER);
        return volunteerBasicService.save(volunteerUser);
    }

    @Before
    public void setUp(){
        // admin1
        Admin admin = saveRandomAdmin();
        adminService.addAreaByUserId(admin.getUser().getUserId(), Region.CENTRAL_DISTRICT);
        adminService.addAreaByUserId(admin.getUser().getUserId(), Region.DALSEO_DISTRICT);

        Admin admin1 = saveRandomAdmin();
        adminService.addAreaByUserId(admin1.getUser().getUserId(), Region.DALSEONGGUN_DISTRICT);

        saveRandomAdmin();

        Admin superAdmin = saveRandomSuperAdmin();
        adminService.addAreaByUserId(superAdmin.getUser().getUserId(), Region.DALSEONGGUN_DISTRICT);
        adminService.addAreaByUserId(superAdmin.getUser().getUserId(), Region.CENTRAL_DISTRICT);
        adminService.addAreaByUserId(superAdmin.getUser().getUserId(), Region.EAST_DISTRICT);

        saveRandomVolunteer();
        saveRandomVolunteer();
        saveRandomVolunteer();
        saveRandomVolunteer();

        System.out.println("세팅이 완료 되었습니다.");
    }


    @After
    public void tearDown() {
        System.out.println("Test is Complete");
    }


    @Test
    public void 정상_유저에서_관리자로_등록하기() {

        // 실행
        userService.registerAdmin(userId);

        // 권한이 ADMIN으로 바뀌었는 지?
        Authority authority = userService.getAuthorityByUserId(userId);
        assertThat(authority).isEqualTo(Authority.ROLE_ADMIN);

        // ADMIN Table에 정상적으로 추가가 되었는지?
        Admin admin = adminService.findAdminByUserId(userId);
        assertThat(admin.getUser()
                        .getUserId()).isEqualTo(userId);

    }

    @Test(expected = UserNullException.class)
    public void 없는_유저를_관리자로_등록하기() {

        // 실행
        userService.registerAdmin(nonExistedUserId);
    }

    //
    @Test(expected = AlreadyExistedAdmin.class)
    public void 이미_있는_관리자를_관리자로_등록하기() {
        // 먼저 관리자 한 번 실행
        userService.registerAdmin(userId);

        // 다시 실행
        userService.registerAdmin(userId);
    }

    //
    @Test
    public void 정상적으로_관리자를_유저로_강등하기() {

        // 관리자로 업그레이드
        userService.registerAdmin(userId);

        // 실행
        userService.fireAdmin(userId);

        // 권한이 일반 봉사자로 바뀌었는 지 확인
        Authority authority = userService.getAuthorityByUserId(userId);
        assertThat(authority).isEqualTo(Authority.ROLE_VOLUNTEER);

        // 아이디가 지워졌는지 확인
        User user = userService.findUserByUserId(userId);
        boolean ExistedUser = adminService.isExistedUser(user);
        assertFalse(ExistedUser);

        // After을 위해서 임시로 추가..
        userService.registerAdmin(userId);
    }

    @Test(expected = UserNullException.class)
    public void 없는_유저를_강등하기() {
        userService.fireAdmin(nonExistedUserId);
    }

    @Test(expected = AdminNullException.class)
    public void 기존에_관리자가_아니었던_사람_강등하기() {
        userService.fireAdmin(userId);
    }

    @Test
    public void 유저_정보_불러오기(){
        System.out.println("result: " + userService.findAllUserAuthInfos());
    }

}
