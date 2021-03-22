package com.api.yirang.matching.application;

import com.api.yirang.apply.application.ApplyAdvancedService;
import com.api.yirang.apply.repository.persistence.maria.ApplyDao;
import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.auth.domain.user.model.Volunteer;
import com.api.yirang.common.support.type.Sex;
import com.api.yirang.matching.model.mongo.UnMatchingList;
import com.api.yirang.matching.repository.maria.MatchingRepository;
import com.api.yirang.matching.repository.mongo.UnMatchingListRepository;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.notices.repository.persistence.maria.ActivityDao;
import com.api.yirang.seniors.domain.senior.model.Senior;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MatchingService {


    // service DI
    private final ApplyAdvancedService applyAdvancedService;

    private final ApplyDao applyDao;
    private final UserService userService;
    private final ActivityDao activityDao;

    private final MatchingRepository matchingRepository;
    private final UnMatchingListRepository unMatchingListRepository;

    public List<Activity> findAllActivityTomorrow(LocalDateTime now){
        LocalDateTime kstNow = now.plusHours(9L);

        LocalDateTime kstTomorrowStart = kstNow.plusHours(15L);
        LocalDateTime kstTomorrowEnd = kstTomorrowStart.plusDays(1L);

        return activityDao.findActivitiesByDtovBetween(kstTomorrowStart, kstTomorrowEnd);
    }

    public void executeMatchingSteps(Activity activity){
        final Queue<Volunteer> maleWorkVolunteers = new LinkedList<>();
        final Queue<Volunteer> femaleWorkVolunteers= new LinkedList<>();
        final Queue<Volunteer> maleTalkVolunteers= new LinkedList<>();
        final Queue<Volunteer> femaleTalkVolunteers = new LinkedList<>();

        final Queue<Senior> maleWorkSeniors = new LinkedList<>();
        final Queue<Senior> femaleWorkSeniors = new LinkedList<>();
        final Queue<Senior> maleTalkSeniors= new LinkedList<>();
        final Queue<Senior> femaleTalkSeniors = new LinkedList<>();

        final List<Long> unMatchedVolunteerIds = new ArrayList<>();
        final List<Long> unMatchedSeniorIds = new ArrayList<>();

        initVolunteers(activity, maleWorkVolunteers, femaleWorkVolunteers, maleTalkVolunteers, femaleTalkVolunteers);
        initSeniors(activity, maleWorkSeniors, femaleWorkSeniors, maleTalkSeniors, femaleTalkSeniors);

        // Update UnMatched
        unMatchingListRepository.save(UnMatchingList.builder()
                                                    .activityId(activity.getActivityId())
                                                    .volunteerIds(unMatchedVolunteerIds)
                                                    .seniorIds(unMatchedSeniorIds)
                                                    .build());
    }

    private void initVolunteers(Activity activity,
                                Queue<Volunteer> maleWorkVolunteers, Queue<Volunteer> femaleWorkVolunteers,
                                Queue<Volunteer> maleTalkVolunteers, Queue<Volunteer> femaleTalkVolunteers){

        // 1. 해당하는 activity에 Work로 신청한 봉사자 데이터 가져오기
        List<Volunteer> workVolunteers = applyAdvancedService.getWorkVolunteersFromActivityId(activity);
        // 2. 해당하는 Activity에 talk로 신청한 봉사자 데이터 가져오기
        List<Volunteer> talkVolunteers = applyAdvancedService.getTalkVolunteersFromActivityId(activity);

        maleWorkVolunteers.addAll(
                workVolunteers.stream().filter(e -> e.getUser().getSex().equals(Sex.SEX_MALE)).collect(Collectors.toList())
        );

        femaleWorkVolunteers.addAll(
                workVolunteers.stream().filter(e -> e.getUser().getSex().equals(Sex.SEX_FEMALE)).collect(Collectors.toList())
        );

        maleTalkVolunteers.addAll(
                talkVolunteers.stream().filter(e -> e.getUser().getSex().equals(Sex.SEX_MALE)).collect(Collectors.toList())
        );

        femaleTalkVolunteers.addAll(
                talkVolunteers.stream().filter(e -> e.getUser().getSex().equals(Sex.SEX_FEMALE)).collect(Collectors.toList())
        );

    }

    private void initSeniors(Activity activity,
                             Queue<Senior> maleWorkSeniors, Queue<Senior> femaleWorkSeniors,
                             Queue<Senior> maleTalkSeniors, Queue<Senior> femaleTalkSeniors){

        // 1. 해당하는 activity에 Work로 되어있는 피봉사자 데이터 가져오기
        List<Senior> workSeniors =
        // 2. 해당하는 activity에 Talk로 되어있는 피봉사자 데이터 가져오기
    }



    public void goOnStepFirst(Activity activity){
        // 1. 남자 피봉사자 - 노력 봉사 불러오기

        // 2. 남자 봉사자 - 피봉사자 불러오기


    }




    /// private
    // TODO: 해당하는 피봉시들 제외

    // TODO: 해당하는 봉사자들 제외


}
