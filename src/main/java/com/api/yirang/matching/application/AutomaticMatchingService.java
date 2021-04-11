package com.api.yirang.matching.application;


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
    private final MatchingLogicService matchingLogicService;

    // 봉사 시작 이틀 전의 봉사를 매칭합니다.
    @Scheduled(cron = "0 0 15 * * *" )
    public void checkTomorrowAfterTomorrowActivityAndExecuteMatching(){
        LocalDateTime now = LocalDateTime.now();
        List<Activity> activities = matchingService.findAllActivityTomorrow(now);

        // 해당하는Activites가 없으면 그냥 넘김
        if (activities.isEmpty()){
            return;
        }
        activities.forEach(matchingLogicService::executeMatchingSteps);

    }


}