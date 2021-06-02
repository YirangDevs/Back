package com.api.yirang.auth.repository;

import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.generator.UserGenerator;
import com.api.yirang.auth.repository.persistence.maria.UserDao;
import com.api.yirang.auth.support.type.Authority;
import com.api.yirang.common.support.type.Sex;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class UserDaoTest {

    @Autowired
    UserDao userDao;

    @Autowired
    UserService userService;

    @Test
    public void 가짜_유저_저장(){
        userService.saveUser(UserGenerator.createRandomUser(Authority.ROLE_VOLUNTEER, Sex.SEX_MALE));
        userService.saveUser(UserGenerator.createRandomUser(Authority.ROLE_VOLUNTEER, Sex.SEX_MALE));

        userService.saveUser(UserGenerator.createRandomUser(Authority.ROLE_VOLUNTEER, Sex.SEX_FEMALE));
        userService.saveUser(UserGenerator.createRandomUser(Authority.ROLE_VOLUNTEER, Sex.SEX_FEMALE));


    }

    @Test
    public void 유저데이터_저장_후_불러오기(){
        long userId = 123;
        String username = "jeongminYoo";
        Sex sex = Sex.SEX_MALE;
        String email = "likemin0142@naver.com";
        Authority authority = Authority.ROLE_ADMIN;

        userDao.save(
                User.builder()
                    .userId(userId)
                    .username(username)
                    .sex(sex)
                    .email(email)
                    .authority(authority)
                    .build()
        );

        User user = userDao.findByUserId(userId).get();

        System.out.println("User: " + user);

        assertThat(user.getUserId()).isEqualTo(userId);
        assertThat(user.getUsername()).isEqualTo(username);
        assertThat(user.getSex()).isEqualTo(sex);
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getAuthority()).isEqualTo(authority);
    }
}
