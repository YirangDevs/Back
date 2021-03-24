package com.api.yirang.matching.application;

import com.api.yirang.matching.model.maria.Matching;
import com.api.yirang.notices.domain.activity.model.Activity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MatchingServiceTest {

    @Autowired
    private MatchingService matchingService;

    @Test
    public void 내일_봉사_찾기(){
        // current
        LocalDateTime currentTime = LocalDateTime.of(2021, 1, 29, 7, 0 ,0);
        List<Activity> activities = matchingService.findAllActivityTomorrow(currentTime);
        System.out.println("activities: " + activities);
    }

}
