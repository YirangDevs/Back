package com.api.yirang.auth.repository;


import com.api.yirang.auth.domain.user.model.Admin;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.repository.persistence.maria.AdminDao;
import com.api.yirang.auth.repository.persistence.maria.UserDao;
import com.api.yirang.auth.support.type.Authority;
import com.api.yirang.common.repository.persistence.maria.DistributionRegionDao;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminDaoTest {

    @Autowired
    AdminDao adminDao;

    @Autowired
    UserDao userDao;

    @Autowired
    DistributionRegionDao distributionRegionDao;

    @After
    public void cleanUp() {adminDao.deleteAll();}

    @Test
    @Transactional
    public void 관리자데이터_저장_후_불러오기(){
        long userId = 123;
        String username = "jeongminYoo";
        String sex = "male";
        String email = "likemin0142@naver.com";
        Authority authority = Authority.ROLE_ADMIN;

        User user = User.builder()
                        .userId(userId)
                        .username(username)
                        .sex(sex)
                        .email(email)
                        .authority(authority)
                        .build();
        System.out.println("User: " + user);

        Long adminNumber = adminDao.save(
                        Admin.builder()
                             .user(user)
                             .build()
                            ).getAdminNumber();

        Admin admin = adminDao.findAdminByAdminNumber(adminNumber).get();
        System.out.println("Admin: " + admin);

        assertThat(admin.getUser().getUserId()).isEqualTo(userId);
    }
}
