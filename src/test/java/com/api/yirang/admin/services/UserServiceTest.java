package com.api.yirang.admin.services;

import com.api.yirang.auth.application.basicService.AdminService;
import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.auth.domain.user.exceptions.AlreadyExistedAdmin;
import com.api.yirang.auth.domain.user.exceptions.UserNullException;
import com.api.yirang.auth.domain.user.model.Admin;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.repository.persistence.maria.AdminDao;
import com.api.yirang.auth.repository.persistence.maria.UserDao;
import com.api.yirang.auth.support.type.Authority;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    // 실험하고자 하는 Service
    @Autowired
    UserService userService;

    // DB 확인용
    @Autowired
    AdminService adminService;

    // Mock Variable
    long userId = 1468416139;
    long nonExistedUserId = 1234;

    @After
    public void tearDown(){
        System.out.println("Test is Complete");
        User user = userService.findUserByUserId(userId);
        adminService.delete(user);
        userService.updateAuthority(userId, Authority.ROLE_VOLUNTEER);
    }


    @Test
    public void 정상_유저에서_관리자로_등록하기(){

        // 실행
        userService.registerAdmin(userId);

        // 권한이 ADMIN으로 바뀌었는 지?
        Authority authority = userService.getAuthorityByUserId(userId);
        assertThat(authority).isEqualTo(Authority.ROLE_ADMIN);

        // ADMIN Table에 정상적으로 추가가 되었는지?
        Admin admin = adminService.findAdminByUserId(userId);
        assertThat(admin.getUser().getUserId()).isEqualTo(userId);

    }

    // Error 예상한다.
    @Test(expected = UserNullException.class)
    public void 없는_유저를_관리자로_등록하기(){

        // 실행
        userService.registerAdmin(nonExistedUserId);

    }

    //
    @Test(expected = AlreadyExistedAdmin.class)
    public void 이미_있는_관리자를_관리자로_등록하기(){
        // 먼저 관리자 한 번 실행
        userService.registerAdmin(userId);

        // 다시 실행
        userService.registerAdmin(userId);
    }
}
