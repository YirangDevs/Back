package com.api.yirang.email.application;

import com.api.yirang.auth.application.intermediateService.UserService;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final UserService userService;
    private final JavaMailSender javaMailSender;

    private final String emailHost = "yirang@gmail.com";
    private final String senderName = "Yirang";
    private final String siteUrl = "http://ec2-3-35-99-114.ap-northeast-2.compute.amazonaws.com:8080";


    public void sendVerificationEmail(String toEmail) throws UnsupportedEncodingException, MessagingException {

        String subject = "이메일을 인증해주십쇼";
        String content = "보낸다 [[name]],<br>"
                               + "너의 강함을 뽑내주기 바란다.:)<br>"
                               + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                               + "고맙다...,<br> "
                               + "이랑";

        // Content
        content = content.replace("[[name]]", "너에게~");
//        content = content.replace("[[URL]]", siteUrl+"/verify?code="+ 암호);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(emailHost, senderName);
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(content, true);

        javaMailSender.send(message);

    }

}
