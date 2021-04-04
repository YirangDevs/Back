package com.api.yirang.matching.application;

import com.api.yirang.apply.application.ApplyAdvancedService;
import com.api.yirang.apply.application.ApplyBasicService;
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

    public void executeMatchingSteps(Activity activity) {


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

        final List<Long> unMatchedVolunteerIds = new ArrayList<>();
        final List<Long> unMatchedSeniorIds = new ArrayList<>();

        initVolunteers(activity, maleWorkVolunteers, femaleWorkVolunteers, maleTalkVolunteers, femaleTalkVolunteers);
        initSeniors(activity, maleWorkSeniors, femaleWorkSeniors, maleTalkSeniors, femaleTalkSeniors, seniorMatchingNumMap, seniorMaxMatchingNumMap);


        goOnStepFirst(maleWorkVolunteers, femaleWorkVolunteers, maleTalkVolunteers, femaleTalkVolunteers,
                      maleWorkSeniors, femaleWorkSeniors, maleTalkSeniors, femaleTalkSeniors,
                      workSeniors, talkSeniors,
                      workVolunteers, talkVolunteers,
                      unMatchedVolunteerIds, unMatchedSeniorIds, activity, seniorMatchingNumMap);

        goOnStepSecond(maleWorkVolunteers, femaleWorkVolunteers, maleTalkVolunteers, femaleTalkVolunteers,
                       maleWorkSeniors, femaleWorkSeniors, maleTalkSeniors, femaleTalkSeniors,
                       workSeniors, talkSeniors,
                       workVolunteers, talkVolunteers,
                       unMatchedVolunteerIds, unMatchedSeniorIds, activity, seniorMatchingNumMap);
        goOnStepThird(maleWorkVolunteers, femaleWorkVolunteers, maleTalkVolunteers, femaleTalkVolunteers,
                      maleWorkSeniors, femaleWorkSeniors, maleTalkSeniors, femaleTalkSeniors,
                      workSeniors, talkSeniors, bothSeniors,
                      workVolunteers, talkVolunteers,
                      unMatchedVolunteerIds, unMatchedSeniorIds, activity, seniorMatchingNumMap);
        goOnStepFourth(workSeniors, talkSeniors, bothSeniors,
                       workVolunteers, talkVolunteers, bothVolunteers,
                       unMatchedSeniorIds, activity, seniorMatchingNumMap);
        goOnStepFifth(workSeniors, talkSeniors, bothSeniors,
                      workVolunteers, talkVolunteers, bothVolunteers,
                      unMatchedSeniorIds, activity, seniorMatchingNumMap);
        goOnStepSixth(workSeniors, talkSeniors, bothSeniors,
                      workVolunteers, talkVolunteers, bothVolunteers,
                      unMatchedVolunteerIds, unMatchedSeniorIds, activity, seniorMatchingNumMap, seniorMaxMatchingNumMap);

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
                             Map<Long, Integer> seniorMaxMatchingNumMap){

        // 1. 해당하는 activity에 Work로 되어있는 피봉사자 데이터 가져오기
        List<Senior> workSeniors = volunteerServiceBasicService.getWorkSeniorsFromActivity(activity);
        workSeniors.forEach(s -> seniorMatchingNumMap.put(s.getSeniorId(), 0));
        workSeniors.forEach(s -> seniorMaxMatchingNumMap.put(s.getSeniorId(), volunteerServiceBasicService.findVolunteerServiceByActivityAndSenior(activity, s).getNumsOfRequiredVolunteers().intValue()));
        // 2. 해당하는 activity에 Talk로 되어있는 피봉사자 데이터 가져오기
        List<Senior> talkSeniors = volunteerServiceBasicService.getTalkSeniorsFromActivity(activity);
        talkSeniors.forEach(s -> seniorMatchingNumMap.put(s.getSeniorId(), 0));
        talkSeniors.forEach(s -> seniorMaxMatchingNumMap.put(s.getSeniorId(), volunteerServiceBasicService.findVolunteerServiceByActivityAndSenior(activity, s).getNumsOfRequiredVolunteers().intValue()));

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
                               List<Long> unMatchedVolunteerIds, List<Long> unMatchedSeniorIds, Activity activity, Map<Long, Integer> seniorMatchingNumMap) {

        matchingProcess(maleWorkVolunteers, maleWorkSeniors, workSeniors, activity, seniorMatchingNumMap);

        if (maleWorkSeniors.isEmpty()){

            // 남자-노력-봉사자 > 남자-노력-피봉사자일 때, 남자-노력-봉사자들을 남자-말벗-봉사자들의 후순위로 넣음
            if (!maleWorkVolunteers.isEmpty()){
                maleTalkVolunteers.addAll(maleWorkVolunteers);
            }
            matchingProcess(maleTalkVolunteers, maleTalkSeniors, talkSeniors, activity, seniorMatchingNumMap);
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
        matchingProcess(maleTalkVolunteers, maleWorkSeniors, workSeniors, activity, seniorMatchingNumMap);

        if (maleWorkSeniors.isEmpty()){

            if (maleTalkVolunteers.isEmpty()){
                // 남자 - 말벗 - 봉사자까지 다 사용했을 경우
                // 나머지 전부 제외
                unMatchedSeniorIds.addAll(
                        maleTalkSeniors.stream().map(Senior::getSeniorId).collect(Collectors.toList())
                );
                return;
            }
            matchingProcess(maleTalkVolunteers, maleTalkSeniors, talkSeniors, activity, seniorMatchingNumMap);
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
                                List<Long> unMatchedVolunteerIds, List<Long> unMatchedSeniorIds, Activity activity, Map<Long, Integer> seniorMatchingNumMap) {

        matchingProcess(femaleWorkVolunteers, femaleWorkSeniors, workSeniors, activity, seniorMatchingNumMap);

        if (femaleWorkSeniors.isEmpty()){
            // 여자-노력-피봉사자가 같으나 많으면 종료
            if(!femaleWorkVolunteers.isEmpty()){
                // 남은 여자-노력-봉사자는 workVolunteers로 이동
                workVolunteers.addAll(femaleWorkVolunteers);
            }
            return;
        }

        matchingProcess(workVolunteers, femaleWorkSeniors, workSeniors, activity, seniorMatchingNumMap);

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
                               Map<Long, Integer> seniorMatchingNumMap) {

        // 남은 여자-말벗-봉사자들은 말벗 봉사자들로 편입.
        talkVolunteers.addAll(femaleTalkVolunteers);

        matchingProcess(talkVolunteers, femaleTalkSeniors, talkSeniors, activity, seniorMatchingNumMap);
        if (!femaleTalkSeniors.isEmpty()){
            // 남은 여자-말벗-피봉사자들은 노력과 매칭.
            matchingProcess(workVolunteers, femaleTalkSeniors, talkSeniors, activity, seniorMatchingNumMap);
            if(!femaleTalkSeniors.isEmpty()){
                // 남은 피봉사자들은 제외
                unMatchedSeniorIds.addAll(femaleTalkSeniors.stream().map(Senior::getSeniorId).collect(Collectors.toList()));
            }
        }
    }
    private void goOnStepFourth(PriorityQueue<Senior> workSeniors, PriorityQueue<Senior> talkSeniors, PriorityQueue<Senior> bothSeniors,
                                PriorityQueue<Volunteer> workVolunteers, PriorityQueue<Volunteer> talkVolunteers, PriorityQueue<Volunteer> bothVolunteers,
                                List<Long> unMatchedSeniorIds, Activity activity, Map<Long, Integer> seniorMatchingNumMap) {

        matchingProcess(workVolunteers, workSeniors, bothSeniors, activity, seniorMatchingNumMap);

        if (!workVolunteers.isEmpty()){
            bothVolunteers.addAll(workVolunteers);
            workVolunteers.clear();
        }
    }
    private void goOnStepFifth(PriorityQueue<Senior> workSeniors, PriorityQueue<Senior> talkSeniors, PriorityQueue<Senior> bothSeniors,
                               PriorityQueue<Volunteer> workVolunteers, PriorityQueue<Volunteer> talkVolunteers, PriorityQueue<Volunteer> bothVolunteers,
                               List<Long> unMatchedSeniorIds, Activity activity, Map<Long, Integer> seniorMatchingNumMap) {

        matchingProcess(talkVolunteers, talkSeniors, bothSeniors, activity, seniorMatchingNumMap);

        if (!talkVolunteers.isEmpty()){
            bothVolunteers.addAll(talkVolunteers);
            talkVolunteers.clear();
        }

    }


    private void goOnStepSixth(PriorityQueue<Senior> workSeniors, PriorityQueue<Senior> talkSeniors, PriorityQueue<Senior> bothSeniors,
                               PriorityQueue<Volunteer> workVolunteers, PriorityQueue<Volunteer> talkVolunteers, PriorityQueue<Volunteer> bothVolunteers,
                               List<Long> unMatchedVolunteerIds, List<Long> unMatchedSeniorIds,
                               Activity activity, Map<Long, Integer> seniorMatchingNumMap, Map<Long, Integer> seniorMaxMatchingNumMap) {

        while(!bothVolunteers.isEmpty() && !bothSeniors.isEmpty()){
            Senior senior = bothSeniors.remove();

            if (seniorMaxMatchingNumMap.get(senior.getSeniorId()) >= seniorMatchingNumMap.get(senior.getSeniorId())){
                continue;
            }

            Volunteer volunteer = bothVolunteers.remove();
            matchingCrudService.save(Matching.builder()
                                             .activity(activity)
                                             .senior(senior)
                                             .volunteer(volunteer)
                                             .build());
            seniorMatchingNumMap.put(senior.getSeniorId(), seniorMatchingNumMap.get(senior.getSeniorId()) + 1);
        }

        // 남은 사람은 다 제외
        if (!bothSeniors.isEmpty()){
            unMatchedSeniorIds.addAll(
                    bothSeniors.stream().map(Senior::getSeniorId).collect(Collectors.toList())
            );
        }
        if (!bothVolunteers.isEmpty()){
            unMatchedVolunteerIds.addAll(
                    bothVolunteers.stream().map(Volunteer::getVolunteerNumber).collect(Collectors.toList())
            );
        }
    }

    private void matchingProcess(Queue<Volunteer> volunteerQueue, Queue<Senior> seniorQueue, PriorityQueue<Senior> seniorPQ, Activity activity, Map<Long, Integer> seniorMatchingNumMap) {
        while(!volunteerQueue.isEmpty() && !seniorQueue.isEmpty()){
            Senior senior = seniorQueue.remove();
            Volunteer volunteer = volunteerQueue.remove();

            matchingCrudService.save(Matching.builder()
                                             .activity(activity)
                                             .senior(senior)
                                             .volunteer(volunteer)
                                             .build());
            seniorMatchingNumMap.put(senior.getSeniorId(), seniorMatchingNumMap.get(senior.getSeniorId()) + 1);
            seniorPQ.add(senior);
        }
    }
}
