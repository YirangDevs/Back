package com.api.yirang.email.application;

import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.common.support.generator.RandomGenerator;
import com.api.yirang.email.dto.EmailRequestDto;
import com.api.yirang.email.dto.EmailValidationRequestDto;
import com.api.yirang.email.dto.EmailValidationResponseDto;
import com.api.yirang.email.exception.*;
import com.api.yirang.email.model.Email;
import com.api.yirang.email.repository.EmailRepository;
import com.api.yirang.email.util.Validation;
import com.google.common.hash.Hashing;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final UserService userService;
    private final JavaMailSender javaMailSender;
    private final MailContentHelper mailContentHelper;
    private final EmailRepository emailRepository;

    private final String emailHost = "yirang@gmail.com";
    private final String senderName = "Yirang";

    // Privates
    private void sendEmail(String toEmail, String subject, String content){
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom(emailHost, senderName);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(content, true);
        }
        catch (Exception ex){
            throw new CustomMessagingException();
        }
        javaMailSender.send(message);
    }


    public void sendVerificationEmail(Long userId){

        // User 찾기
        User user = userService.findUserByUserId(userId);
        // Email 찾기
        Email email = emailRepository.findEmailByUser_UserId(userId).orElseThrow(new EmailNullException(userId));

        // 이미 검증이 된 Email 인 경우 에러
        if (email.getValidation().isValid()){
            throw new EmailAlreadyValidException();
        }

        // Email이 비었거나 Null 이면 에러
        if (user.getEmail() == null || user.getEmail().equals("")){
            throw new EmailAddressNullException();
        }

        // Certification 생성하기
        String certificationNumbers = RandomGenerator.RandomStringOfNumbersWithLength(6);

        // Email 업데이트 하기
        emailRepository.updateEmailCertificationWithUserId(userId, Hashing.sha256().hashString(certificationNumbers, StandardCharsets.UTF_8).toString());

        final String toEmail = user.getEmail();
        final String subject = "[Yirang 재가봉사] 본인 이메일을 인증해주세요";
        final String content = mailContentHelper.generateVerifyMailContent(user, certificationNumbers);

        sendEmail(toEmail, subject, content);
    }


    public EmailValidationResponseDto checkMyValidation(Long userId) {
        User user = userService.findUserByUserId(userId);
        Email email = emailRepository.findEmailByUser_UserId(userId).orElseThrow(new EmailNullException(userId));
        return new EmailValidationResponseDto(email.getValidation());
    }

    public void verifyMyEmail(Long userId, EmailValidationRequestDto emailValidationRequestDto) {
        User user = userService.findUserByUserId(userId);
        Email email = emailRepository.findEmailByUser_UserId(userId).orElseThrow(new EmailNullException(userId));
        String certification = Hashing.sha256().hashString(emailValidationRequestDto.getCertificationNumbers(), StandardCharsets.UTF_8).toString();
        // 이미 검증이 되어있는 경우
        if (email.getValidation().isValid()){
            throw new EmailAlreadyValidException();
        }
        // 만약 Certification number가 일치 하지 않으면 Error
        if (!email.getCertificationNumbers().equals(certification)){
            throw new EmailCertificationFailException();
        }
        // 다 괜찮으면 validation_YES로 바꾸기
        emailRepository.updateEmailVerificationWithUserId(userId, Validation.VALIDATION_YES);
    }
}
