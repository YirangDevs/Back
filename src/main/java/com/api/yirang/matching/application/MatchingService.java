package com.api.yirang.matching.application;

import com.api.yirang.apply.application.ApplyAdvancedService;
import com.api.yirang.apply.repository.persistence.maria.ApplyDao;
import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.auth.domain.user.model.Volunteer;
import com.api.yirang.common.support.type.Sex;
import com.api.yirang.matching.model.maria.Matching;
import com.api.yirang.matching.model.mongo.UnMatchingList;
import com.api.yirang.matching.repository.maria.MatchingRepository;
import com.api.yirang.matching.repository.mongo.UnMatchingListRepository;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.notices.repository.persistence.maria.ActivityDao;
import com.api.yirang.seniors.application.basicService.VolunteerServiceBasicService;
import com.api.yirang.seniors.domain.senior.model.Senior;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MatchingService {


    // service DI
    private final ActivityDao activityDao;

    public List<Activity> findAllActivityTomorrow(LocalDateTime now){

        LocalDateTime tomorrowStart = now.plusHours(33L);
        LocalDateTime tomorrowEnd = tomorrowStart.plusDays(1L);

        return activityDao.findActivitiesByDtovBetween(tomorrowStart, tomorrowEnd);
    }



}
