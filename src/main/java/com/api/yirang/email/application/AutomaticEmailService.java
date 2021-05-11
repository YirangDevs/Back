package com.api.yirang.email.application;


import com.api.yirang.notices.application.basicService.ActivityBasicService;
import com.api.yirang.notices.domain.activity.model.Activity;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.ISBN;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AutomaticEmailService {

    // DI
    private final EmailAdvancedService emailAdvancedService;
    private final ActivityBasicService activityBasicService;


    /**
     * 목적: 내일이 봉사 당일인 Actiivity를 찾아서 안내 메일을 보냄
     */
    @Scheduled(cron = "0 0 13 * * *")
    public void checkTomorrowActivityAndSendEmail(){
        // 1. 내일 봉사날인 Activites를 찾음.
        List<Activity> activityList = activityBasicService.findAllActivityTomorrow(LocalDateTime.now());

        if (activityList.isEmpty()){
            // 따로 Acitivity가 없으면 그냥 넘어감
            return;
        }
        // 2. 각 Activity에 대해서 봉사 안내 메일 로직을 실행함.
        activityList.forEach(emailAdvancedService::sendEmailToVolunteerAboutTomorrowActivity);

    }
}
