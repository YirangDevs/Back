package com.api.yirang.email;


import com.api.yirang.auth.application.intermediateService.EmailService;
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
@ActiveProfiles("test")
public class ServiceTest {

    @Autowired
    EmailService emailService;

    @Test
    public void 이메일_보내기() throws UnsupportedEncodingException, MessagingException {
        emailService.sendVerificationEmail("likemin5517@naver.com");
    }
}
