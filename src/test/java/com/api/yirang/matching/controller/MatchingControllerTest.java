package com.api.yirang.matching.controller;

import com.api.yirang.auth.domain.user.model.Volunteer;
import com.api.yirang.auth.generator.VolunteerGenerator;
import com.api.yirang.auth.repository.persistence.maria.VolunteerDao;
import com.api.yirang.matching.application.MatchingService;
import com.api.yirang.matching.dto.MatchingContentDto;
import com.api.yirang.matching.dto.MatchingRecordsDto;
import com.api.yirang.matching.dto.MatchingResponseDto;
import com.api.yirang.matching.generator.MatchingGenerator;
import com.api.yirang.matching.model.maria.Matching;
import com.api.yirang.matching.model.mongo.UnMatchingList;
import com.api.yirang.matching.repository.maria.MatchingRepository;
import com.api.yirang.matching.repository.mongo.UnMatchingListRepository;
import com.api.yirang.notice.generator.ActivityGenerator;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.notices.repository.persistence.maria.ActivityDao;
import com.api.yirang.senior.generator.SeniorGenerator;
import com.api.yirang.seniors.domain.senior.model.Senior;
import com.api.yirang.seniors.repository.persistence.maria.SeniorDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class MatchingControllerTest {

    @Autowired
    MockMvc mockMvc;

    // Save data to repository
    @Autowired
    VolunteerDao volunteerDao;

    @Autowired
    ActivityDao activityDao;

    @Autowired
    SeniorDao seniorDao;

    @Autowired
    MatchingRepository matchingRepository;

    @Autowired
    UnMatchingListRepository unMatchingListRepository;

    // Test
    @Autowired
    private MatchingService matchingService;


    //
    @Test
    public void 해당하는_activity_테스트(){
        Activity activity = ActivityGenerator.createAndStoreRandomActivity(activityDao);

        Volunteer volunteer1 = VolunteerGenerator.createAndRandomVolunteer(volunteerDao);
        Volunteer volunteer2 = VolunteerGenerator.createAndRandomVolunteer(volunteerDao);
        Volunteer volunteer3 = VolunteerGenerator.createAndRandomVolunteer(volunteerDao);
        Senior senior1 = SeniorGenerator.createAndSaveRandomSenior(seniorDao);
        Senior senior2 = SeniorGenerator.createAndSaveRandomSenior(seniorDao);
        Senior senior3 = SeniorGenerator.createAndSaveRandomSenior(seniorDao);

        Matching matching1 = MatchingGenerator.createAndStoreRandomMatching(matchingRepository, activity, senior1, volunteer1);
        Matching matching2 = MatchingGenerator.createAndStoreRandomMatching(matchingRepository, activity, senior2, volunteer2);
        Matching matching3 = MatchingGenerator.createAndStoreRandomMatching(matchingRepository, activity, senior3, volunteer3);

        System.out.println("matching1: " + matching1);
        System.out.println("matching2: " + matching2);
        System.out.println("matching3: " + matching3);

        MatchingResponseDto matchingResponseDto = matchingService.findMatchingByActivityId(activity.getActivityId());

        List<MatchingContentDto> matchingContentDtos = matchingResponseDto.getMatchingContentDtos();

        assertThat(matchingContentDtos.size()).isEqualTo(3);

        System.out.println("matchingContentDtos: " + matchingContentDtos);

    }

    @Test
    public void findUnMatchingByAcitivityId_테스트하기(){
        Activity activity = ActivityGenerator.createAndStoreRandomActivity(activityDao);

        Volunteer volunteer1 = VolunteerGenerator.createAndRandomVolunteer(volunteerDao);
        Volunteer volunteer2 = VolunteerGenerator.createAndRandomVolunteer(volunteerDao);
        Volunteer volunteer3 = VolunteerGenerator.createAndRandomVolunteer(volunteerDao);
        Senior senior1 = SeniorGenerator.createAndSaveRandomSenior(seniorDao);
        Senior senior2 = SeniorGenerator.createAndSaveRandomSenior(seniorDao);
        Senior senior3 = SeniorGenerator.createAndSaveRandomSenior(seniorDao);

        System.out.println("volunteer1: " + volunteer1);
        System.out.println("volunteer2: " + volunteer2);
        System.out.println("volunteer3: " + volunteer3);
        System.out.println("senior1: " + senior1);
        System.out.println("senior2: " + senior2);
        System.out.println("senior3: " + senior3);

        List<Long> seniorIds = new ArrayList<>();
        seniorIds.add(senior1.getSeniorId());
        seniorIds.add(senior2.getSeniorId());
        seniorIds.add(senior3.getSeniorId());

        List<Long> volunteerIds = new ArrayList<>();
        volunteerIds.add(volunteer1.getVolunteerNumber());
        volunteerIds.add(volunteer2.getVolunteerNumber());
        volunteerIds.add(volunteer3.getVolunteerNumber());


        unMatchingListRepository.save(UnMatchingList.builder()
                                                    .activityId(activity.getActivityId())
                                                    .seniorIds(seniorIds)
                                                    .volunteerIds(volunteerIds)
                                                    .build());

        System.out.println(
                matchingService.findUnMatchingByActivityId(activity.getActivityId()));

        unMatchingListRepository.deleteUnMatchingListByActivityId(activity.getActivityId());
    }

    @Test
    public void findMyMatchingRecords_By_userId_테스트하기(){
        Activity activity1 = ActivityGenerator.createAndStoreRandomActivity(activityDao);
        Activity activity2 = ActivityGenerator.createAndStoreRandomActivity(activityDao);
        Activity activity3 = ActivityGenerator.createAndStoreRandomActivity(activityDao);
        Activity activity4 = ActivityGenerator.createAndStoreRandomActivity(activityDao);

        Volunteer volunteer1 = VolunteerGenerator.createAndRandomVolunteer(volunteerDao);
        Volunteer volunteer2 = VolunteerGenerator.createAndRandomVolunteer(volunteerDao);

        Senior senior1 = SeniorGenerator.createAndSaveRandomSenior(seniorDao);
        Senior senior2 = SeniorGenerator.createAndSaveRandomSenior(seniorDao);
        Senior senior3 = SeniorGenerator.createAndSaveRandomSenior(seniorDao);

        Matching matching1 = MatchingGenerator.createAndStoreRandomMatching(matchingRepository, activity1, senior1, volunteer1);
        Matching matching2 = MatchingGenerator.createAndStoreRandomMatching(matchingRepository, activity2, senior2, volunteer1);
        Matching matching3 = MatchingGenerator.createAndStoreRandomMatching(matchingRepository, activity3, senior3, volunteer1);
        Matching matching4 = MatchingGenerator.createAndStoreRandomMatching(matchingRepository, activity4, senior2, volunteer2);
        Matching matching5 = MatchingGenerator.createAndStoreRandomMatching(matchingRepository, activity3, senior3, volunteer2);

        System.out.println("matching1: " + matching1);
        System.out.println("matching2: " + matching2);
        System.out.println("matching3: " + matching3);
        System.out.println("matching4: " + matching4);
        System.out.println("matching5: " + matching5);

        MatchingRecordsDto matchingRecordsDto = matchingService.findMyMatchingRecordsByUserId(volunteer1.getUser().getUserId());
        System.out.println("matchingRecordsDto: " + matchingRecordsDto);

    }



}
