package com.api.yirang.matching.application;

import com.api.yirang.apply.application.ApplyAdvancedService;
import com.api.yirang.apply.application.ApplyBasicService;
import com.api.yirang.apply.domain.model.Apply;
import com.api.yirang.apply.support.type.MatchingState;
import com.api.yirang.auth.application.basicService.VolunteerBasicService;
import com.api.yirang.auth.domain.user.model.Volunteer;
import com.api.yirang.common.support.type.Sex;
import com.api.yirang.matching.model.maria.Matching;
import com.api.yirang.matching.model.mongo.UnMatchingList;
import com.api.yirang.matching.repository.mongo.UnMatchingListRepository;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.seniors.application.basicService.VolunteerServiceBasicService;
import com.api.yirang.seniors.domain.senior.model.Senior;
import com.api.yirang.seniors.support.custom.ServiceType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MatchingLogicService {

    // DI DAO
    private final UnMatchingListRepository unMatchingListRepository;

    // DI Services
    private final ApplyAdvancedService applyAdvancedService;
    private final ApplyBasicService applyBasicService;
    private final MatchingCrudService matchingCrudService;
    private final VolunteerServiceBasicService volunteerServiceBasicService;
    private final VolunteerBasicService volunteerBasicService;

    public void executeMatchingSteps(Activity activity) {

        // 봉사 타입을 기록하는 Map
        final Map<Long, ServiceType> seniorServiceTypeMap = new HashMap<>();

        // 매칭된 갯수를 기록하는 Map
        final Map<Long, Integer> seniorMatchingNumMap = new HashMap<>();
        final Map<Long, Integer> seniorMaxMatchingNumMap = new HashMap<>();

        // Volunteers 들은 한 번 매칭이 되면 소모되는 Queue
        final Queue<Volunteer> maleWorkVolunteers = new LinkedList<>();
        final Queue<Volunteer> femaleWorkVolunteers = new LinkedList<>();
        final Queue<Volunteer> maleTalkVolunteers = new LinkedList<>();
        final Queue<Volunteer> femaleTalkVolunteers = new LinkedList<>();

        // 아래의 큐들은 아직 한번도 매칭되지 않은 Senior Queue 들 입니다.
        final Queue<Senior> maleWorkSeniors = new LinkedList<>();
        final Queue<Senior> femaleWorkSeniors = new LinkedList<>();
        final Queue<Senior> maleTalkSeniors = new LinkedList<>();
        final Queue<Senior> femaleTalkSeniors = new LinkedList<>();

        // 아래의 큐들은 과정 중에 발생하는 노력, 말벗, Both 봉사 PQ들입니다.
        final PriorityQueue<Volunteer> workVolunteers = new PriorityQueue<>(Comparator.comparing(v -> applyBasicService.getUniqueApplyWithVolunteerAndActivity(v, activity)
                                                                                                                       .getDtoa())); // 먼저 지원한 사람부터 우선순위
        final PriorityQueue<Volunteer> talkVolunteers = new PriorityQueue<>(Comparator.comparing(v -> applyBasicService.getUniqueApplyWithVolunteerAndActivity(v, activity)
                                                                                                                       .getDtoa())); // 먼저 지원한 사람부터 우선순위
        final PriorityQueue<Volunteer> bothVolunteers = new PriorityQueue<>(Comparator.comparing(v -> applyBasicService.getUniqueApplyWithVolunteerAndActivity(v, activity)
                                                                                                                       .getDtoa())); // 먼저 지원한 사람부터 우선순위

        // 아래의 큐는 매칭 과정 중에 발생하는 PQ입니다.
        final PriorityQueue<Senior> workSeniors = new PriorityQueue<>(Comparator.comparingInt((Senior s) -> seniorMatchingNumMap.get(s.getSeniorId()))
                                                                                .thenComparingLong(s -> volunteerServiceBasicService.findVolunteerServiceByActivityAndSenior(activity, s)
                                                                                                                                    .getPriority()));
        final PriorityQueue<Senior> talkSeniors = new PriorityQueue<>(Comparator.comparingInt((Senior s) -> seniorMatchingNumMap.get(s.getSeniorId()))
                                                                                .thenComparingLong(s -> volunteerServiceBasicService.findVolunteerServiceByActivityAndSenior(activity, s)
                                                                                                                                    .getPriority()));
        final PriorityQueue<Senior> bothSeniors = new PriorityQueue<>(Comparator.comparingInt((Senior s) -> seniorMatchingNumMap.get(s.getSeniorId()))
                                                                                .thenComparingLong(s -> volunteerServiceBasicService.findVolunteerServiceByActivityAndSenior(activity, s)
                                                                                                                                    .getPriority()));

        // 최종 결과값들이 저장되는 List 입니다.
        final List<Matching> matchingList = new ArrayList<>();
        final List<Long> unMatchedVolunteerIds = new ArrayList<>();
        final List<Long> unMatchedSeniorIds = new ArrayList<>();

        initVolunteers(activity, maleWorkVolunteers, femaleWorkVolunteers, maleTalkVolunteers, femaleTalkVolunteers);
        initSeniors(activity, maleWorkSeniors, femaleWorkSeniors, maleTalkSeniors, femaleTalkSeniors, seniorMatchingNumMap, seniorServiceTypeMap, seniorMaxMatchingNumMap);

        showLogs("[init]"
                 , maleWorkVolunteers, femaleWorkVolunteers, maleTalkVolunteers, femaleTalkVolunteers,
                 maleWorkSeniors, femaleWorkSeniors, maleTalkSeniors, femaleTalkSeniors,
                 workSeniors, talkSeniors, bothSeniors,
                 workVolunteers, talkVolunteers, bothVolunteers,
                 unMatchedVolunteerIds, unMatchedSeniorIds, activity, seniorMatchingNumMap, seniorMaxMatchingNumMap, seniorServiceTypeMap, matchingList);

        goOnStepFirst(maleWorkVolunteers, femaleWorkVolunteers, maleTalkVolunteers, femaleTalkVolunteers,
                      maleWorkSeniors, femaleWorkSeniors, maleTalkSeniors, femaleTalkSeniors,
                      workSeniors, talkSeniors,
                      workVolunteers, talkVolunteers,
                      unMatchedVolunteerIds, unMatchedSeniorIds, activity, seniorMatchingNumMap, seniorServiceTypeMap, matchingList);

        showLogs("[1st Step]", maleWorkVolunteers, femaleWorkVolunteers, maleTalkVolunteers, femaleTalkVolunteers, maleWorkSeniors, femaleWorkSeniors, maleTalkSeniors, femaleTalkSeniors, workSeniors, talkSeniors, bothSeniors,
                 workVolunteers, talkVolunteers, bothVolunteers,
                 unMatchedVolunteerIds, unMatchedSeniorIds, activity, seniorMatchingNumMap, seniorMaxMatchingNumMap, seniorServiceTypeMap, matchingList);

        goOnStepSecond(maleWorkVolunteers, femaleWorkVolunteers, maleTalkVolunteers, femaleTalkVolunteers,
                       maleWorkSeniors, femaleWorkSeniors, maleTalkSeniors, femaleTalkSeniors,
                       workSeniors, talkSeniors,
                       workVolunteers, talkVolunteers,
                       unMatchedVolunteerIds, unMatchedSeniorIds, activity, seniorMatchingNumMap, seniorServiceTypeMap, matchingList);

        showLogs("[2nd Step]", maleWorkVolunteers, femaleWorkVolunteers, maleTalkVolunteers, femaleTalkVolunteers, maleWorkSeniors, femaleWorkSeniors, maleTalkSeniors, femaleTalkSeniors, workSeniors, talkSeniors, bothSeniors,
                 workVolunteers, talkVolunteers, bothVolunteers,
                 unMatchedVolunteerIds, unMatchedSeniorIds, activity, seniorMatchingNumMap, seniorMaxMatchingNumMap, seniorServiceTypeMap, matchingList);


        goOnStepThird(maleWorkVolunteers, femaleWorkVolunteers, maleTalkVolunteers, femaleTalkVolunteers,
                      maleWorkSeniors, femaleWorkSeniors, maleTalkSeniors, femaleTalkSeniors,
                      workSeniors, talkSeniors, bothSeniors,
                      workVolunteers, talkVolunteers,
                      unMatchedVolunteerIds, unMatchedSeniorIds, activity, seniorMatchingNumMap, seniorServiceTypeMap, matchingList);

        showLogs("[3rd Step]", maleWorkVolunteers, femaleWorkVolunteers, maleTalkVolunteers, femaleTalkVolunteers, maleWorkSeniors, femaleWorkSeniors, maleTalkSeniors, femaleTalkSeniors, workSeniors, talkSeniors, bothSeniors,
                 workVolunteers, talkVolunteers, bothVolunteers,
                 unMatchedVolunteerIds, unMatchedSeniorIds, activity, seniorMatchingNumMap, seniorMaxMatchingNumMap, seniorServiceTypeMap, matchingList);

        goOnStepFourth(workSeniors, talkSeniors, bothSeniors,
                       workVolunteers, talkVolunteers, bothVolunteers,
                       unMatchedSeniorIds, activity, seniorMatchingNumMap, seniorServiceTypeMap, matchingList);

        showLogs("[4th Step]", maleWorkVolunteers, femaleWorkVolunteers, maleTalkVolunteers, femaleTalkVolunteers, maleWorkSeniors, femaleWorkSeniors, maleTalkSeniors, femaleTalkSeniors, workSeniors, talkSeniors, bothSeniors,
                 workVolunteers, talkVolunteers, bothVolunteers,
                 unMatchedVolunteerIds, unMatchedSeniorIds, activity, seniorMatchingNumMap, seniorMaxMatchingNumMap, seniorServiceTypeMap, matchingList);

        goOnStepFifth(workSeniors, talkSeniors, bothSeniors,
                      workVolunteers, talkVolunteers, bothVolunteers,
                      unMatchedSeniorIds, activity, seniorMatchingNumMap, seniorServiceTypeMap, matchingList);

        showLogs("[5th Step]", maleWorkVolunteers, femaleWorkVolunteers, maleTalkVolunteers, femaleTalkVolunteers, maleWorkSeniors, femaleWorkSeniors, maleTalkSeniors, femaleTalkSeniors, workSeniors, talkSeniors, bothSeniors,
                 workVolunteers, talkVolunteers, bothVolunteers,
                 unMatchedVolunteerIds, unMatchedSeniorIds, activity, seniorMatchingNumMap, seniorMaxMatchingNumMap, seniorServiceTypeMap, matchingList);

        goOnStepSixth(workSeniors, talkSeniors, bothSeniors,
                      workVolunteers, talkVolunteers, bothVolunteers,
                      unMatchedVolunteerIds, unMatchedSeniorIds, activity, seniorMatchingNumMap, seniorMaxMatchingNumMap, seniorServiceTypeMap, matchingList);

        showLogs("[6th Step]", maleWorkVolunteers, femaleWorkVolunteers, maleTalkVolunteers, femaleTalkVolunteers, maleWorkSeniors, femaleWorkSeniors, maleTalkSeniors, femaleTalkSeniors, workSeniors, talkSeniors, bothSeniors,
                 workVolunteers, talkVolunteers, bothVolunteers,
                 unMatchedVolunteerIds, unMatchedSeniorIds, activity, seniorMatchingNumMap, seniorMaxMatchingNumMap, seniorServiceTypeMap, matchingList);

        // Update UnMatched
        matchingCrudService.saveUniqueUnMatchingListRepository(UnMatchingList.builder()
                                                    .activityId(activity.getActivityId())
                                                    .volunteerIds(unMatchedVolunteerIds)
                                                    .seniorIds(unMatchedSeniorIds)
                                                    .build());

        // Insert data in MatchingList (bulk job)
        matchingCrudService.saveAll(matchingList);

        // update apply
        matchingList.forEach(matching -> {
            Apply apply = applyBasicService.getUniqueApplyWithVolunteerAndActivity(matching.getVolunteer(), activity);
            applyBasicService.updateMatchingStateByApplyId(apply.getApplyId(), MatchingState.MATCHING_SUCCESS);
        });

        unMatchedVolunteerIds.forEach(volunteerId -> {
            Apply apply = applyBasicService.getUniqueApplyWithVolunteerAndActivity(volunteerBasicService.findVolunteerByVolunteerNumber(volunteerId), activity);
            applyBasicService.updateMatchingStateByApplyId(apply.getApplyId(), MatchingState.MATCHING_FAIL);
        });

    }




    private void initVolunteers(Activity activity,
                                Queue<Volunteer> maleWorkVolunteers, Queue<Volunteer> femaleWorkVolunteers,
                                Queue<Volunteer> maleTalkVolunteers, Queue<Volunteer> femaleTalkVolunteers){

        // 1. 해당하는 activity에 Work로 신청한 봉사자 데이터 가져오기
        List<Volunteer> workVolunteers = applyAdvancedService.getWorkVolunteersFromActivity(activity);
        // 2. 해당하는 activity에 talk로 신청한 봉사자 데이터 가져오기
        List<Volunteer> talkVolunteers = applyAdvancedService.getTalkVolunteersFromActivity(activity);

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
                             Queue<Senior> maleTalkSeniors, Queue<Senior> femaleTalkSeniors,
                             Map<Long, Integer> seniorMatchingNumMap,
                             Map<Long, ServiceType> seniorServiceTypeMap,
                             Map<Long, Integer> seniorMaxMatchingNumMap){

        // 1. 해당하는 activity에 Work로 되어있는 피봉사자 데이터 가져오기
        List<Senior> workSeniors = volunteerServiceBasicService.getWorkSeniorsFromActivity(activity);
        workSeniors.forEach(s -> seniorMatchingNumMap.put(s.getSeniorId(), 0));
        workSeniors.forEach(s -> seniorMaxMatchingNumMap.put(s.getSeniorId(), volunteerServiceBasicService.findVolunteerServiceByActivityAndSenior(activity, s).getNumsOfRequiredVolunteers().intValue()));
        workSeniors.forEach(s -> seniorServiceTypeMap.put(s.getSeniorId(), volunteerServiceBasicService.findVolunteerServiceByActivityAndSenior(activity, s).getServiceType()));
        // 2. 해당하는 activity에 Talk로 되어있는 피봉사자 데이터 가져오기
        List<Senior> talkSeniors = volunteerServiceBasicService.getTalkSeniorsFromActivity(activity);
        talkSeniors.forEach(s -> seniorMatchingNumMap.put(s.getSeniorId(), 0));
        talkSeniors.forEach(s -> seniorMaxMatchingNumMap.put(s.getSeniorId(), volunteerServiceBasicService.findVolunteerServiceByActivityAndSenior(activity, s).getNumsOfRequiredVolunteers().intValue()));
        talkSeniors.forEach(s -> seniorServiceTypeMap.put(s.getSeniorId(), volunteerServiceBasicService.findVolunteerServiceByActivityAndSenior(activity, s).getServiceType()));

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
                               PriorityQueue<Senior> workSeniors, PriorityQueue<Senior> talkSeniors,
                               PriorityQueue<Volunteer> workVolunteers, PriorityQueue<Volunteer> talkVolunteers,
                               List<Long> unMatchedVolunteerIds, List<Long> unMatchedSeniorIds, Activity activity,
                               Map<Long, Integer> seniorMatchingNumMap, Map<Long, ServiceType> seniorServiceTypeMap, List<Matching> matchingList) {

        matchingProcess(maleWorkVolunteers, maleWorkSeniors, workSeniors, activity, seniorMatchingNumMap, seniorServiceTypeMap, matchingList);

        if (maleWorkSeniors.isEmpty()){

            // 남자-노력-봉사자 > 남자-노력-피봉사자일 때, 남자-노력-봉사자들을 남자-말벗-봉사자들의 후순위로 넣음
            if (!maleWorkVolunteers.isEmpty()){
                maleTalkVolunteers.addAll(maleWorkVolunteers);
            }
            matchingProcess(maleTalkVolunteers, maleTalkSeniors, talkSeniors, activity, seniorMatchingNumMap, seniorServiceTypeMap, matchingList);
            if (maleTalkVolunteers.isEmpty() && maleTalkSeniors.isEmpty()){
                return;
            }
            if (maleTalkVolunteers.isEmpty()){
                // 남은 남자-피봉사자는 un-matched list에 넣음
                unMatchedSeniorIds.addAll(
                        maleTalkSeniors.stream().map(Senior::getSeniorId).collect(Collectors.toList())
                );
            }
            // 남은 남자-말벗-봉사는 step 4,5에 사용함
            // 말벗, 노력에 따라 각각 workVolunteers, talkVolunteers에 넣음
            maleTalkVolunteers.stream()
                              .filter(v -> applyBasicService.getUniqueApplyWithVolunteerAndActivity(v, activity).getServiceType().equals(ServiceType.SERVICE_WORK))
                              .forEach(workVolunteers::add);
            maleTalkVolunteers.stream()
                              .filter(v -> applyBasicService.getUniqueApplyWithVolunteerAndActivity(v, activity).getServiceType().equals(ServiceType.SERVICE_TALK))
                              .forEach(talkVolunteers::add);
            return;
        }
        // 남자 - 노력 - 피봉사자 > 남자 - 노력 - 봉사자인 경우
        matchingProcess(maleTalkVolunteers, maleWorkSeniors, workSeniors, activity, seniorMatchingNumMap, seniorServiceTypeMap, matchingList);

        if (maleWorkSeniors.isEmpty()){

            if (maleTalkVolunteers.isEmpty()){
                // 남자 - 말벗 - 봉사자까지 다 사용했을 경우
                // 나머지 전부 제외
                unMatchedSeniorIds.addAll(
                        maleTalkSeniors.stream().map(Senior::getSeniorId).collect(Collectors.toList())
                );
                return;
            }
            matchingProcess(maleTalkVolunteers, maleTalkSeniors, talkSeniors, activity, seniorMatchingNumMap, seniorServiceTypeMap, matchingList);
            if (maleTalkVolunteers.isEmpty() && maleTalkSeniors.isEmpty()){
                return;
            }
            if (maleTalkVolunteers.isEmpty()){
                unMatchedSeniorIds.addAll(
                        maleTalkSeniors.stream().map(Senior::getSeniorId).collect(Collectors.toList())
                );
            }
            // 남은 남자-말벗-봉사는 step 4,5에 사용함
            talkVolunteers.addAll(maleTalkVolunteers);
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
                                Queue<Senior> maleWorkSeniors, Queue<Senior> femaleWorkSeniors,
                                Queue<Senior> maleTalkSeniors, Queue<Senior> femaleTalkSeniors,
                                PriorityQueue<Senior> workSeniors, PriorityQueue<Senior> talkSeniors, PriorityQueue<Volunteer> workVolunteers, PriorityQueue<Volunteer> talkVolunteers,
                                List<Long> unMatchedVolunteerIds, List<Long> unMatchedSeniorIds, Activity activity,
                                Map<Long, Integer> seniorMatchingNumMap, Map<Long, ServiceType> seniorServiceTypeMap, List<Matching> matchingList) {

        matchingProcess(femaleWorkVolunteers, femaleWorkSeniors, workSeniors, activity, seniorMatchingNumMap, seniorServiceTypeMap, matchingList);

        if (femaleWorkSeniors.isEmpty()){
            // 여자-노력-피봉사자가 같으나 많으면 종료
            if(!femaleWorkVolunteers.isEmpty()){
                // 남은 여자-노력-봉사자는 workVolunteers로 이동
                workVolunteers.addAll(femaleWorkVolunteers);
            }
            return;
        }

        matchingProcess(workVolunteers, femaleWorkSeniors, workSeniors, activity, seniorMatchingNumMap, seniorServiceTypeMap, matchingList);

        if (!femaleWorkSeniors.isEmpty()){
            // 남은 여자-노력-피봉사자들은 제외
            unMatchedSeniorIds.addAll(femaleWorkSeniors.stream().map(Senior::getSeniorId).collect(Collectors.toList()));
        }
    }

    private void goOnStepThird(Queue<Volunteer> maleWorkVolunteers, Queue<Volunteer> femaleWorkVolunteers, Queue<Volunteer> maleTalkVolunteers, Queue<Volunteer> femaleTalkVolunteers,
                               Queue<Senior> maleWorkSeniors, Queue<Senior> femaleWorkSeniors, Queue<Senior> maleTalkSeniors, Queue<Senior> femaleTalkSeniors,
                               PriorityQueue<Senior> workSeniors, PriorityQueue<Senior> talkSeniors,
                               PriorityQueue<Senior> bothSeniors, PriorityQueue<Volunteer> workVolunteers, PriorityQueue<Volunteer> talkVolunteers,
                               List<Long> unMatchedVolunteerIds, List<Long> unMatchedSeniorIds, Activity activity,
                               Map<Long, Integer> seniorMatchingNumMap, Map<Long, ServiceType> seniorServiceTypeMap, List<Matching> matchingList) {

        // 남은 여자-말벗-봉사자들은 말벗 봉사자들로 편입.
        talkVolunteers.addAll(femaleTalkVolunteers);

        matchingProcess(talkVolunteers, femaleTalkSeniors, talkSeniors, activity, seniorMatchingNumMap, seniorServiceTypeMap, matchingList);
        if (!femaleTalkSeniors.isEmpty()){
            // 남은 여자-말벗-피봉사자들은 노력과 매칭.
            matchingProcess(workVolunteers, femaleTalkSeniors, talkSeniors, activity, seniorMatchingNumMap, seniorServiceTypeMap, matchingList);
            if(!femaleTalkSeniors.isEmpty()){
                // 남은 피봉사자들은 제외
                unMatchedSeniorIds.addAll(femaleTalkSeniors.stream().map(Senior::getSeniorId).collect(Collectors.toList()));
            }
        }
    }
    private void goOnStepFourth(PriorityQueue<Senior> workSeniors, PriorityQueue<Senior> talkSeniors, PriorityQueue<Senior> bothSeniors,
                                PriorityQueue<Volunteer> workVolunteers, PriorityQueue<Volunteer> talkVolunteers, PriorityQueue<Volunteer> bothVolunteers,
                                List<Long> unMatchedSeniorIds, Activity activity, Map<Long, Integer> seniorMatchingNumMap, Map<Long, ServiceType> seniorServiceTypeMap, List<Matching> matchingList) {

        matchingProcess(workVolunteers, workSeniors, bothSeniors, activity, seniorMatchingNumMap, seniorServiceTypeMap, matchingList);

        if (!workVolunteers.isEmpty()){
            bothVolunteers.addAll(workVolunteers);
            workVolunteers.clear();
        }
        if (!workSeniors.isEmpty()){
            bothSeniors.addAll(workSeniors);
            workSeniors.clear();
        }

    }
    private void goOnStepFifth(PriorityQueue<Senior> workSeniors, PriorityQueue<Senior> talkSeniors, PriorityQueue<Senior> bothSeniors,
                               PriorityQueue<Volunteer> workVolunteers, PriorityQueue<Volunteer> talkVolunteers, PriorityQueue<Volunteer> bothVolunteers,
                               List<Long> unMatchedSeniorIds, Activity activity, Map<Long, Integer> seniorMatchingNumMap, Map<Long, ServiceType> seniorServiceTypeMap, List<Matching> matchingList) {

        matchingProcess(talkVolunteers, talkSeniors, bothSeniors, activity, seniorMatchingNumMap, seniorServiceTypeMap, matchingList);

        if (!talkVolunteers.isEmpty()){
            bothVolunteers.addAll(talkVolunteers);
            talkVolunteers.clear();
        }
        if (!talkSeniors.isEmpty()){
            bothSeniors.addAll(talkSeniors);
            talkSeniors.clear();
        }

    }


    private void goOnStepSixth(PriorityQueue<Senior> workSeniors, PriorityQueue<Senior> talkSeniors, PriorityQueue<Senior> bothSeniors,
                               PriorityQueue<Volunteer> workVolunteers, PriorityQueue<Volunteer> talkVolunteers, PriorityQueue<Volunteer> bothVolunteers,
                               List<Long> unMatchedVolunteerIds, List<Long> unMatchedSeniorIds,
                               Activity activity, Map<Long, Integer> seniorMatchingNumMap, Map<Long, Integer> seniorMaxMatchingNumMap, Map<Long, ServiceType> seniorServiceTypeMap, List<Matching> matchingList) {

        while(!bothVolunteers.isEmpty() && !bothSeniors.isEmpty()){
            Senior senior = bothSeniors.remove();

            if (seniorMaxMatchingNumMap.get(senior.getSeniorId()) <= seniorMatchingNumMap.get(senior.getSeniorId())){
                continue;
            }

            Volunteer volunteer = bothVolunteers.remove();
            matchingList.add(Matching.builder()
                                     .activity(activity)
                                     .senior(senior)
                                     .volunteer(volunteer)
                                     .serviceType(seniorServiceTypeMap.get(senior.getSeniorId()))
                                     .build());
            seniorMatchingNumMap.put(senior.getSeniorId(), seniorMatchingNumMap.get(senior.getSeniorId()) + 1);
            bothSeniors.add(senior);
        }

        // 남은 사람은 다 제외
        if (!bothSeniors.isEmpty()){
            unMatchedSeniorIds.addAll(
                    bothSeniors.stream()
                               .filter(s -> seniorMatchingNumMap.get(s.getSeniorId()).equals(0))
                               .map(Senior::getSeniorId).collect(Collectors.toList())
            );
        }
        if (!bothVolunteers.isEmpty()){
            unMatchedVolunteerIds.addAll(
                    bothVolunteers.stream().map(Volunteer::getVolunteerNumber).collect(Collectors.toList())
            );
        }
    }

    private void matchingProcess(Queue<Volunteer> volunteerQueue, Queue<Senior> seniorQueue, PriorityQueue<Senior> seniorPQ, Activity activity,
                                 Map<Long, Integer> seniorMatchingNumMap, Map<Long, ServiceType> seniorServiceTypeMap, List<Matching> matchingList) {
        while(!volunteerQueue.isEmpty() && !seniorQueue.isEmpty()){
            Senior senior = seniorQueue.remove();
            Volunteer volunteer = volunteerQueue.remove();

            matchingList.add(Matching.builder()
                                     .activity(activity)
                                     .senior(senior)
                                     .serviceType(seniorServiceTypeMap.get(senior.getSeniorId()))
                                     .volunteer(volunteer)
                                     .build());

            seniorMatchingNumMap.put(senior.getSeniorId(), seniorMatchingNumMap.get(senior.getSeniorId()) + 1);
            seniorPQ.add(senior);
        }
    }
    private void showLogs(String step,
                          Queue<Volunteer> maleWorkVolunteers, Queue<Volunteer> femaleWorkVolunteers, Queue<Volunteer> maleTalkVolunteers, Queue<Volunteer> femaleTalkVolunteers,
                          Queue<Senior> maleWorkSeniors, Queue<Senior> femaleWorkSeniors, Queue<Senior> maleTalkSeniors, Queue<Senior> femaleTalkSeniors,
                          PriorityQueue<Senior> workSeniors, PriorityQueue<Senior> talkSeniors, PriorityQueue<Senior> bothSeniors, PriorityQueue<Volunteer> workVolunteers,
                          PriorityQueue<Volunteer> talkVolunteers, PriorityQueue<Volunteer> bothVolunteers, List<Long> unMatchedVolunteerIds, List<Long> unMatchedSeniorIds,
                          Activity activity, Map<Long, Integer> seniorMatchingNumMap, Map<Long, Integer> seniorMaxMatchingNumMap, Map<Long, ServiceType> seniorServiceTypeMap,
                          List<Matching> matchingList) {


        System.out.println(step + " maleWorkVolunteers: " + maleWorkVolunteers);
        System.out.println(step + " femaleWorkVolunteers: " + femaleWorkVolunteers);
        System.out.println(step + " maleTalkVolunteers: " + maleTalkVolunteers);
        System.out.println(step + " femaleTalkVolunteers: " + femaleTalkVolunteers);

        System.out.println(step + " maleWorkSeniors: " + maleWorkSeniors);
        System.out.println(step + " femaleWorkSeniors: " + femaleWorkSeniors);
        System.out.println(step + " maleTalkSeniors: " + maleTalkSeniors);
        System.out.println(step + " femaleTalkSeniors: " + femaleTalkSeniors);

        System.out.println(step + " workSeniors: " + workSeniors);
        System.out.println(step + " talkSeniors: " + talkSeniors);
        System.out.println(step + " bothSeniors: " + bothSeniors);
        System.out.println(step + " workVolunteers: " + workVolunteers);
        System.out.println(step + " talkVolunteers: " + talkVolunteers);
        System.out.println(step + " bothVolunteers: " + bothVolunteers);
        System.out.println(step + " unMatchedVolunteerIds: " + unMatchedVolunteerIds);
        System.out.println(step + " unMatchedSeniorIds: " + unMatchedSeniorIds);
        System.out.println(step + " seniorMatchingNumMap: " + seniorMatchingNumMap);
        System.out.println(step + " seniorMaxMatchingNumMap: " + seniorMaxMatchingNumMap);
        System.out.println(step + " seniorServiceTypeMap: " + seniorServiceTypeMap);
        System.out.println(step + " matchingList: " + matchingList);
    }
}
