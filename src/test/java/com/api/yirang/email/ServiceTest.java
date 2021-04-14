package com.api.yirang.email;


import com.api.yirang.auth.support.type.Authority;
import com.api.yirang.common.support.type.Region;
import com.api.yirang.common.support.type.Sex;
import com.api.yirang.email.application.EmailAdvancedService;
import com.api.yirang.email.application.EmailService;
import com.api.yirang.email.dto.ActivityGuideMailContent;
import com.api.yirang.email.dto.MatchingMailContent;
import com.api.yirang.email.dto.UserWithdrawMailContent;
import com.api.yirang.matching.application.MatchingService;
import com.api.yirang.matching.model.maria.Matching;
import com.api.yirang.matching.repository.maria.MatchingRepository;
import com.api.yirang.notices.domain.notice.model.Notice;
import com.api.yirang.notices.repository.persistence.maria.NoticeDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceTest {

    @Autowired
    EmailService emailService;

    @Autowired
    EmailAdvancedService emailAdvancedService;

    @Autowired
    MatchingService matchingService;

    @Autowired
    MatchingRepository matchingRepository;

    @Autowired
    NoticeDao noticeDao;

    @Test
    public void 인증_이메일_보내기() throws UnsupportedEncodingException, MessagingException {
        emailService.sendVerificationEmail(Long.valueOf(1467876478));
    }

    @Test
    public void 매칭_이메일_보내기(){
        List<MatchingMailContent> matchingMailContentList = new LinkedList<>();

        matchingMailContentList.add(
                MatchingMailContent.builder()
                                   .region(Region.CENTRAL_DISTRICT)
                                   .seniorName("Hello")
                                   .dtov(LocalDateTime.now())
                                   .phoneNumber("1234")
                                   .build());
        matchingMailContentList.add(
                MatchingMailContent.builder()
                                   .region(Region.CENTRAL_DISTRICT)
                                   .seniorName("Hel123lo")
                                   .dtov(LocalDateTime.now())
                                   .phoneNumber("1234234")
                                   .build());

        emailService.sendMatchingEmail(1468416139L, matchingMailContentList);
    }

    @Test
    public void 탈퇴_이메일_보내기(){
        List<MatchingMailContent> matchingMailContentList = new LinkedList<>();

        matchingMailContentList.add(
                MatchingMailContent.builder()
                                   .region(Region.CENTRAL_DISTRICT)
                                   .seniorName("Hello")
                                   .dtov(LocalDateTime.now())
                                   .phoneNumber("1234")
                                   .build());
        matchingMailContentList.add(
                MatchingMailContent.builder()
                                   .region(Region.CENTRAL_DISTRICT)
                                   .seniorName("Hel123lo")
                                   .dtov(LocalDateTime.now())
                                   .phoneNumber("1234234")
                                   .build());

        UserWithdrawMailContent userWithdrawMailContent = UserWithdrawMailContent.builder()
                                                                                 .name("HI")
                                                                                 .sex(Sex.SEX_FEMALE)
                                                                                 .authority(Authority.ROLE_ADMIN)
                                                                                 .phoneNumber("1234")
                                                                                 .build();

        emailService.sendWithdrawEmail(1468416139L, userWithdrawMailContent, matchingMailContentList);
    }

    @Test
    public void 가이드_이메일_보내기(){
        Matching matching = matchingRepository.findById(618L).orElse(null);

        emailAdvancedService.sendEmailToVolunteerAboutSuccessEmail(matching);
    }

    @Test
    public void 매칭실패_이메일(){


    }

    @Test
    public void 지역추천_이메일(){
        Notice notice = noticeDao.findById(339L).orElse(null);

        emailAdvancedService.sendEmailToVolunteerAboutRecommendedActivity(notice);
    }



}
