package com.api.yirang.matching.application;

import com.api.yirang.auth.application.basicService.VolunteerBasicService;
import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.auth.domain.user.model.Volunteer;
import com.api.yirang.common.support.time.TimeConverter;
import com.api.yirang.img.application.ImgService;
import com.api.yirang.matching.dto.*;
import com.api.yirang.matching.model.maria.Matching;
import com.api.yirang.matching.model.mongo.UnMatchingList;
import com.api.yirang.notices.domain.activity.exception.ActivityNullException;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.notices.repository.persistence.maria.ActivityDao;
import com.api.yirang.seniors.application.basicService.SeniorBasicService;
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
    private final ImgService imgService;


    public MatchingResponseDto findMatchingByActivityId(Long activityId) {
        Activity activity = activityDao.findById(activityId).orElseThrow(ActivityNullException::new);
        List<Matching> matchingList = matchingCrudService.findMatchingsByActivity(activity, false);

        List<MatchingContentDto> matchingContentDtos = matchingList.stream()
                                                                   .map(matching ->{
                                                                       Senior senior = matching.getSenior();
                                                                       Volunteer volunteer = matching.getVolunteer();

                                                                       return MatchingContentDto.builder()
                                                                                                .seniorId(senior.getSeniorId())
                                                                                                .seniorName(senior.getSeniorName())
                                                                                                .seniorSex(senior.getSex())
                                                                                                .seniorPhone(senior.getPhone())
                                                                                                .volunteerName(volunteer.getUser().getRealname())
                                                                                                .volunteerId(volunteer.getUser().getUserId())
                                                                                                .volunteerEmail(volunteer.getUser().getEmail())
                                                                                                .volunteerSex(volunteer.getUser().getSex())
                                                                                                .volunteerPhone(volunteer.getUser().getPhone())
                                                                                                .volunteerProfileImg(imgService.getMyImgNullable(volunteer.getUser().getUserId()))
                                                                                                .serviceType(matching.getServiceType())
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
        List<Volunteer> volunteers = volunteerIds.size() == 0 ? null : volunteerIds.stream().map(volunteerBasicService::findVolunteerByVolunteerNumber).collect(Collectors.toList());


        List<UnMatchingContentDto> unMatchedSeniors = seniors == null ? null : seniors.stream()
                                                                                      .map(s -> UnMatchingContentDto.builder()
                                                                                                                    .id(s.getSeniorId())
                                                                                                                    .name(s.getSeniorName())
                                                                                                                    .sex(s.getSex())
                                                                                                                    .phone(s.getPhone())
                                                                                                                    .build())
                                                                                      .collect(Collectors.toList());

        List<UnMatchingContentDto> unMatchedVolunteers = volunteers == null ? null : volunteers.stream()
                                                                                               .map(v -> UnMatchingContentDto.builder()
                                                                                                                             .id(v.getUser().getUserId())
                                                                                                                             .name(v.getUser().getRealname())
                                                                                                                             .sex(v.getUser().getSex())
                                                                                                                             .phone(v.getUser().getPhone())
                                                                                                                             .img(imgService.getMyImgNullable(v.getUser().getUserId()))
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
                                                                .filter(m -> m.getActivity().getDtov().isBefore(LocalDateTime.now()))
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
