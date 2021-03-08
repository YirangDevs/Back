package com.api.yirang.email;

import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.repository.persistence.maria.UserDao;
import com.api.yirang.email.model.Email;
import com.api.yirang.email.repository.EmailRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class EmailMigration {


    @Autowired
    EmailRepository emailRepository;

    @Autowired
    UserDao userDao;

    @Test
    public void 이메일_레포_생성하기(){
        List<User> users = userDao.findAll();

        users.forEach(e->{emailRepository.save(Email.builder().user(e).build());});
    }
}
