package com.api.yirang.email.application;

import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.common.support.generator.RandomGenerator;
import com.api.yirang.email.dto.EmailNotifiableRequestDto;
import com.api.yirang.email.dto.EmailValidationRequestDto;
import com.api.yirang.email.dto.EmailValidationResponseDto;
import com.api.yirang.email.dto.*;
import com.api.yirang.email.exception.*;
import com.api.yirang.email.model.Email;
import com.api.yirang.email.repository.EmailRepository;
import com.api.yirang.email.util.Validation;
import com.google.common.hash.Hashing;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailService {

    private final UserService userService;
    private final JavaMailSender javaMailSender;
    private final MailContentHelper mailContentHelper;
    private final EmailRepository emailRepository;

    private static final String EMAIL_HOST = "yirang@gmail.com";
    private static final String SENDER_NAME = "Yirang";

    private static final String MATCHING_MAIL_TITLE = "[Yirang 재가봉사] 매칭완료 안내 메일입니다.";
    private static final String VERIFY_MAIL_TITLE = "[Yirang 재가봉사] 본인 이메일을 인증해주세요";
    private static final String WITHDRAW_MAIL_TITLE = "[Yirang 재가봉사] 회원탈퇴 안내 메일입니다.";


    // Privates
    private void sendEmail(String toEmail, String subject, String content){
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom(EMAIL_HOST, SENDER_NAME);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(content, true);
        }
        catch (Exception ex){
            throw new CustomMessagingException();
        }
        javaMailSender.send(message);
    }

    public void sendMatchingEmail(Long userId,
                                  List<MatchingMailContent> matchingMailContents){

        User user = userService.findUserByUserId(userId);

        final String toEmail = user.getEmail();
        final String subject = MATCHING_MAIL_TITLE;
        final String content = mailContentHelper.generateMatchingMailContent(matchingMailContents);

        sendEmail(toEmail, subject, content);

    }

    public void sendWithdrawEmail(Long userId,
                                  UserWithdrawMailContent userWithdrawMailContent,
                                  List<MatchingMailContent> matchingMailContentList) {

        User user = userService.findUserByUserId(userId);

        final String toEmail = user.getEmail();
        final String subject = WITHDRAW_MAIL_TITLE;
        final String content = mailContentHelper.generateWithdrawMailContent(userWithdrawMailContent, matchingMailContentList);

        sendEmail(toEmail, subject, content);
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
        final String subject = VERIFY_MAIL_TITLE;
        final String content = mailContentHelper.generateVerifyMailContent(user, certificationNumbers);

        sendEmail(toEmail, subject, content);
    }


    public EmailValidationResponseDto checkMyValidation(Long userId) {
        User user = userService.findUserByUserId(userId);
        Email email = findEmailByUserId(userId);
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


    // Simple CRUD operations
    public Email findEmailByUserId(Long userId){
        return emailRepository.findEmailByUser_UserId(userId).orElseThrow(new EmailNullException(userId));
    }

    public void changeMyNotification(Long userId, EmailNotifiableRequestDto emailNotifiableRequest) {
        User user = userService.findUserByUserId(userId);
        Email email = findEmailByUserId(userId);
        emailRepository.updateEmailNotificationWithUserId(userId, emailNotifiableRequest.getNotifiable());
    }

}
