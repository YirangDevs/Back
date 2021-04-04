package com.api.yirang.matching.application;


import com.api.yirang.email.application.EmailAdvancedService;
import com.api.yirang.notices.domain.activity.model.Activity;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AutomaticMatchingService {

    private final MatchingService matchingService;
    private final EmailAdvancedService emailAdvancedService;

    @Scheduled(cron = "0 0 0 * * *" )
    public void checkTomorrowAfterTomorrowActivityAndExecuteMatching(){
        LocalDateTime now = LocalDateTime.now();
        List<Activity> activities = matchingService.findAllActivityTomorrow(now);

        // 완료된 Activites가 없으면 그냥 넘김
        if (activities.isEmpty()){
            return;
        }
        // 매칭 진행하기
        activities.forEach(matchingService::executeMatchingSteps);

        //매칭 완료 페이지 보내기
        activities.forEach(emailAdvancedService::sendEmailToAdminAboutMatching);
    }


}
