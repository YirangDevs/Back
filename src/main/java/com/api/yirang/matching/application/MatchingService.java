package com.api.yirang.matching.application;

import com.api.yirang.apply.application.ApplyAdvancedService;
import com.api.yirang.apply.repository.persistence.maria.ApplyDao;
import com.api.yirang.auth.application.basicService.VolunteerBasicService;
import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.domain.user.model.Volunteer;
import com.api.yirang.common.support.time.TimeConverter;
import com.api.yirang.common.support.type.Sex;
import com.api.yirang.matching.dto.*;
import com.api.yirang.matching.model.maria.Matching;
import com.api.yirang.matching.model.mongo.UnMatchingList;
import com.api.yirang.matching.repository.maria.MatchingRepository;
import com.api.yirang.matching.repository.mongo.UnMatchingListRepository;
import com.api.yirang.notices.domain.activity.exception.ActivityNullException;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.notices.repository.persistence.maria.ActivityDao;
import com.api.yirang.seniors.application.basicService.SeniorBasicService;
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
    private final MatchingCrudService matchingCrudService;
    private final SeniorBasicService seniorBasicService;
    private final VolunteerBasicService volunteerBasicService;
    private final UserService userService;

    public List<Activity> findAllActivityTomorrow(LocalDateTime now){

        LocalDateTime tomorrowStart = now.plusHours(33L);
        LocalDateTime tomorrowEnd = tomorrowStart.plusDays(1L);

        return activityDao.findActivitiesByDtovBetween(tomorrowStart, tomorrowEnd);
    }


    public MatchingResponseDto findMatchingByActivityId(Long activityId) {
        Activity activity = activityDao.findById(activityId).orElseThrow(ActivityNullException::new);
        List<Matching> matchingList = matchingCrudService.findMatchingsByActivity(activity);

        List<MatchingContentDto> matchingContentDtos = matchingList.stream()
                                                                   .map(matching ->{
                                                                       Senior senior = matching.getSenior();
                                                                       Volunteer volunteer = matching.getVolunteer();

                                                                       return MatchingContentDto.builder()
                                                                                                .seniorId(senior.getSeniorId())
                                                                                                .seniorName(senior.getSeniorName())
                                                                                                .volunteerName(volunteer.getUser().getRealname())
                                                                                                .volunteerId(volunteer.getUser().getUserId())
                                                                                                .build();
                                                                        }

                                                                   )
                                                                   .collect(Collectors.toList());
        return MatchingResponseDto.builder()
                                  .matchingContentDtos(matchingContentDtos)
                                  .build();

    }

    public UnMatchingResponseDto findUnMatchingByActivityId(Long activityId) {
        Activity activity = activityDao.findById(activityId).orElseThrow(ActivityNullException::new);
        UnMatchingList unMatchingList = matchingCrudService.findUnmatchingByActivityId(activityId);

        List<Long> seniorIds = unMatchingList.getSeniorIds();
        List<Long> volunteerIds = unMatchingList.getVolunteerIds();

        List<Senior> seniors = seniorIds.size() == 0 ? null : seniorIds.stream().map(seniorBasicService::findSeniorById).collect(Collectors.toList());
        List<Volunteer> volunteers = volunteerIds.size() == 0 ? null : seniorIds.stream().map(volunteerBasicService::findVolunteerByVolunteerNumber).collect(Collectors.toList());


        List<UnMatchingContentDto> unMatchedSeniors = seniors == null ? null : seniors.stream()
                                                                                      .map(s -> UnMatchingContentDto.builder()
                                                                                                                    .id(s.getSeniorId())
                                                                                                                    .name(s.getSeniorName())
                                                                                                                    .build())
                                                                                      .collect(Collectors.toList());

        List<UnMatchingContentDto> unMatchedVolunteers = volunteers == null ? null : volunteers.stream()
                                                                                               .map(v -> UnMatchingContentDto.builder()
                                                                                                                             .id(v.getUser().getUserId())
                                                                                                                             .name(v.getUser().getRealname())
                                                                                                                             .build())
                                                                                               .collect(Collectors.toList());

        return UnMatchingResponseDto.builder()
                                    .unMatchedSeniors(unMatchedSeniors)
                                    .unMatchedVolunteers(unMatchedVolunteers)
                                    .build();
    }

    public MatchingRecordsDto findMyMatchingRecordsByUserId(Long userId) {
        // check user
        userService.findUserByUserId(userId);

        List<Matching> matchingList = matchingCrudService.findMatchingsByUserId(userId);

        return MatchingRecordsDto.builder()
                                 .matchingRecordDtoList(matchingList.stream()
                                                                .map(m -> MatchingRecordDto.builder()
                                                                                           .dtom(TimeConverter.LocalDateTimeToString(m.getDtom()))
                                                                                           .dtov(TimeConverter.LocalDateTimeToString(m.getActivity().getDtov()))
                                                                                           .serviceType(m.getServiceType())
                                                                                           .region(m.getActivity().getRegion())
                                                                                           .build())
                                                                .collect(Collectors.toList()))
                                 .build();
    }
}
