package com.api.yirang.email;


import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.email.application.EmailService;
import com.api.yirang.email.model.Email;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceTest {

    @Autowired
    EmailService emailService;


    @Test
    public void 이메일_보내기() throws UnsupportedEncodingException, MessagingException {
        emailService.sendVerificationEmail(Long.valueOf(1468416139));
    }

}
