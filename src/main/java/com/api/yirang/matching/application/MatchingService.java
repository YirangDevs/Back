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
    private final ApplyAdvancedService applyAdvancedService;

    private final ApplyDao applyDao;
    private final UserService userService;
    private final ActivityDao activityDao;
    private final MatchingCrudService matchingCrudService;
    private final VolunteerServiceBasicService volunteerServiceBasicService;

    private final UnMatchingListRepository unMatchingListRepository;

    public List<Activity> findAllActivityTomorrow(LocalDateTime now){
        LocalDateTime kstNow = now.plusHours(9L);

        LocalDateTime kstTomorrowStart = kstNow.plusHours(15L);
        LocalDateTime kstTomorrowEnd = kstTomorrowStart.plusDays(1L);

        return activityDao.findActivitiesByDtovBetween(kstTomorrowStart, kstTomorrowEnd);
    }

    public void executeMatchingSteps(Activity activity){

        // Volunteers 들은 한 번 매칭이 되면 소모되는 Queue
        final Queue<Volunteer> maleWorkVolunteers = new LinkedList<>();
        final Queue<Volunteer> femaleWorkVolunteers= new LinkedList<>();
        final Queue<Volunteer> maleTalkVolunteers= new LinkedList<>();
        final Queue<Volunteer> femaleTalkVolunteers = new LinkedList<>();


        // 아래의 큐들은 아직 한번도 매칭되지 않은 Queue 들 입니다.
        final Queue<Senior> maleWorkSeniors = new LinkedList<>();
        final Queue<Senior> femaleWorkSeniors = new LinkedList<>();
        final Queue<Senior> maleTalkSeniors= new LinkedList<>();
        final Queue<Senior> femaleTalkSeniors = new LinkedList<>();

        //TODO: 아래의 큐들은 과정 중에 발생하는 노력 봉사와 말벗 봉사 Queue 들입니다.
        final PriorityQueue<Volunteer> workVolunteers = new PriorityQueue<>(Comparator.comparing(s -> applyAdvancedService.))

        //TODO: 아래의 큐들은 과정 중에 발생하는 PQ입니다.
        final PriorityQueue<Senior> seniorPQ = new PriorityQueue<>(Comparator.comparingLong(s -> volunteerServiceBasicService.findVolunteerServiceByActivityAndSenior(activity, s).getPriority()));

        final List<Long> unMatchedVolunteerIds = new ArrayList<>();
        final List<Long> unMatchedSeniorIds = new ArrayList<>();

        initVolunteers(activity, maleWorkVolunteers, femaleWorkVolunteers, maleTalkVolunteers, femaleTalkVolunteers);
        initSeniors(activity, maleWorkSeniors, femaleWorkSeniors, maleTalkSeniors, femaleTalkSeniors);



        goOnStepFirst(maleWorkVolunteers, femaleWorkVolunteers, maleTalkVolunteers, femaleTalkVolunteers,
                      maleWorkSeniors, femaleWorkSeniors, maleTalkSeniors, femaleTalkSeniors,
                      seniorPQ, unMatchedVolunteerIds, unMatchedSeniorIds, activity);
        goOnStepSecond(maleWorkVolunteers, femaleWorkVolunteers, maleTalkVolunteers, femaleTalkVolunteers,
                      maleWorkSeniors, femaleWorkSeniors, maleTalkSeniors, femaleTalkSeniors,
                      seniorPQ, unMatchedVolunteerIds, unMatchedSeniorIds, activity);
        goOnStepThird(maleWorkVolunteers, femaleWorkVolunteers, maleTalkVolunteers, femaleTalkVolunteers,
                      maleWorkSeniors, femaleWorkSeniors, maleTalkSeniors, femaleTalkSeniors,
                      seniorPQ, unMatchedVolunteerIds, unMatchedSeniorIds, activity);
        goOnStepFourth(maleWorkVolunteers, femaleWorkVolunteers, maleTalkVolunteers, femaleTalkVolunteers,
                      maleWorkSeniors, femaleWorkSeniors, maleTalkSeniors, femaleTalkSeniors,
                      seniorPQ, unMatchedVolunteerIds, unMatchedSeniorIds, activity);

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
        List<Senior> workSeniors = volunteerServiceBasicService.getWorkSeniorsFromActivity(activity);
        // 2. 해당하는 activity에 Talk로 되어있는 피봉사자 데이터 가져오기
        List<Senior> talkSeniors = volunteerServiceBasicService.getTalkSeniorsFromActivity(activity);

        maleWorkSeniors.addAll(
                workSeniors.stream().filter(e -> e.getSex().equals(Sex.SEX_MALE)).collect(Collectors.toList())
        );
        femaleWorkSeniors.addAll(
                workSeniors.stream().filter(e -> e.getSex().equals(Sex.SEX_FEMALE)).collect(Collectors.toList())
        );
        maleTalkSeniors.addAll(
                talkSeniors.stream().filter(e -> e.getSex().equals(Sex.SEX_MALE)).collect(Collectors.toList())
        );
        femaleTalkSeniors.addAll(
                talkSeniors.stream().filter(e -> e.getSex().equals(Sex.SEX_FEMALE)).collect(Collectors.toList())
        );
    }

    private void goOnStepFirst(Queue<Volunteer> maleWorkVolunteers, Queue<Volunteer> femaleWorkVolunteers, Queue<Volunteer> maleTalkVolunteers, Queue<Volunteer> femaleTalkVolunteers,
                               Queue<Senior> maleWorkSeniors, Queue<Senior> femaleWorkSeniors, Queue<Senior> maleTalkSeniors, Queue<Senior> femaleTalkSeniors,
                               PriorityQueue<Senior> seniorPQ, List<Long> unMatchedVolunteerIds, List<Long> unMatchedSeniorIds, Activity activity) {

        matchingProcess(maleWorkVolunteers, maleWorkSeniors, seniorPQ, activity);

        if (maleWorkSeniors.isEmpty()){

            // 남자-노력-봉사자 > 남자-노력-피봉사자일 때, 남자-노력-봉사자들을 남자-말벗-봉사자들의 후순위로 넣음
            if (!maleWorkVolunteers.isEmpty()){
                maleTalkVolunteers.addAll(maleWorkVolunteers);
            }
            matchingProcess(maleTalkVolunteers, maleTalkSeniors, seniorPQ, activity);
            if (maleTalkVolunteers.isEmpty() && maleTalkSeniors.isEmpty()){
                return;
            }
            if (maleTalkVolunteers.isEmpty()){
                unMatchedSeniorIds.addAll(
                        maleTalkSeniors.stream().map(Senior::getSeniorId).collect(Collectors.toList())
                );
            }
            // 남은 남자-말벗-봉사는 step3에 사용함
            return;
        }
        // 남자 - 노력 - 피봉사자 > 남자 - 노력 - 봉사자
        matchingProcess(maleTalkVolunteers, maleWorkSeniors, seniorPQ, activity);

        if (maleWorkSeniors.isEmpty()){

            if (maleTalkVolunteers.isEmpty()){
                // 남자 - 말벗 - 봉사자까지 다 사용했을 경우
                unMatchedSeniorIds.addAll(
                    maleTalkSeniors.stream().map(Senior::getSeniorId).collect(Collectors.toList())
                );
                return;
            }
            matchingProcess(maleTalkVolunteers, maleTalkSeniors, seniorPQ, activity);
            if (maleTalkVolunteers.isEmpty() && maleTalkSeniors.isEmpty()){
                return;
            }
            if (maleTalkVolunteers.isEmpty()){
                unMatchedSeniorIds.addAll(
                        maleTalkSeniors.stream().map(Senior::getSeniorId).collect(Collectors.toList())
                );
            }
            // 남은 남자-말벗-봉사는 step3에 사용함
            return;
        }
        // 남은 남자 피봉사자들 전부 제외
        unMatchedSeniorIds.addAll(
                maleWorkSeniors.stream().map(Senior::getSeniorId).collect(Collectors.toList())
        );
        unMatchedSeniorIds.addAll(
                maleTalkSeniors.stream().map(Senior::getSeniorId).collect(Collectors.toList())
        );
    }

    private void goOnStepSecond(Queue<Volunteer> maleWorkVolunteers, Queue<Volunteer> femaleWorkVolunteers, Queue<Volunteer> maleTalkVolunteers, Queue<Volunteer> femaleTalkVolunteers,
                                Queue<Senior> maleWorkSeniors, Queue<Senior> femaleWorkSeniors, Queue<Senior> maleTalkSeniors, Queue<Senior> femaleTalkSeniors,
                                PriorityQueue<Senior> seniorPQ, List<Long> unMatchedVolunteerIds, List<Long> unMatchedSeniorIds, Activity activity) {

        matchingProcess(maleWorkVolunteers, femaleWorkSeniors, seniorPQ, activity);

        if (femaleWorkSeniors.isEmpty()){
            // 여자-노력-피봉사자가 많거나 같으면 종료
            return;
        }

        matchingProcess(femaleWorkVolunteers, femaleWorkSeniors, seniorPQ, activity);

        if (!femaleWorkSeniors.isEmpty()){
                unMatchedSeniorIds.addAll(
                    femaleWorkSeniors.stream().map(Senior::getSeniorId).collect(Collectors.toList())
            );
        }

        // 남은 여자-노력-피봉사자는 여자-말벗-피봉사자로 편입
        if(!femaleTalkVolunteers.isEmpty()){
            femaleTalkVolunteers.addAll(
                femaleWorkVolunteers
            );
        }
    }

    private void goOnStepThird(Queue<Volunteer> maleWorkVolunteers, Queue<Volunteer> femaleWorkVolunteers, Queue<Volunteer> maleTalkVolunteers, Queue<Volunteer> femaleTalkVolunteers,
                               Queue<Senior> maleWorkSeniors, Queue<Senior> femaleWorkSeniors, Queue<Senior> maleTalkSeniors, Queue<Senior> femaleTalkSeniors,
                               PriorityQueue<Senior> seniorPQ, List<Long> unMatchedVolunteerIds, List<Long> unMatchedSeniorIds, Activity activity) {

        matchingProcess(femaleTalkVolunteers, femaleTalkSeniors, seniorPQ, activity);
        matchingProcess(maleTalkVolunteers, femaleTalkSeniors, seniorPQ, activity);
    }






    private void matchingProcess(Queue<Volunteer> volunteerQueue, Queue<Senior> seniorQueue, PriorityQueue<Senior> seniorPQ, Activity activity) {
        while(!volunteerQueue.isEmpty() && !seniorQueue.isEmpty()){
            Senior senior = seniorQueue.remove();
            Volunteer volunteer = volunteerQueue.remove();

            matchingCrudService.save(Matching.builder()
                                             .activity(activity)
                                             .senior(senior)
                                             .volunteer(volunteer)
                                             .build());
            seniorPQ.add(senior);
        }
    }

}
