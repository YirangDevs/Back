package com.api.yirang.senior.IntegrationTesting;

import com.api.yirang.common.generator.EnumGenerator;
import com.api.yirang.common.generator.NumberRandomGenerator;
import com.api.yirang.common.generator.StringRandomGenerator;
import com.api.yirang.common.generator.TimeGenerator;
import com.api.yirang.common.support.time.TimeConverter;
import com.api.yirang.common.support.type.Region;
import com.api.yirang.notice.generator.ActivityGenerator;
import com.api.yirang.notices.application.basicService.ActivityBasicService;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.seniors.application.advancedService.SeniorVolunteerAdvancedService;
import com.api.yirang.seniors.presentation.dto.request.RegisterSeniorRequestDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

/**
 * Created by JeongminYoo on 2020/12/5
 * Work with Yirang
 * Email: likemin0142@gmail.com
 * Blog: http://Beewoom.github.io
 * Github: http://github.com/Biewoom
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class SeniorActivityIntegrationTest {

    // Test 하고 싶은 서비스
    @Autowired
    SeniorVolunteerAdvancedService seniorVolunteerAdvancedService;

    // Helper DI
    @Autowired
    ActivityBasicService activityBasicService;

    // variables
    Region region = Region.SOOSEONG_DISTRICT;
    LocalDateTime localDateTime = TimeGenerator.generateRandomLocalDateTime();
    String dov = TimeConverter.LocalDateTimeToString(localDateTime).split(" ")[0];


    // Activity 만들기
    private Activity makeActivity(){
        return ActivityGenerator.createRandomActivity(region, localDateTime);
    }

    private RegisterSeniorRequestDto makeRegisterSeniorRequestDto(){
        return RegisterSeniorRequestDto.builder()
                                       .name(StringRandomGenerator.generateKoreanNameWithLength(Long.valueOf(3)))
                                       .phone(StringRandomGenerator.generateNumericStringWithLength(Long.valueOf(11)))
                                       .sex(EnumGenerator.generateRandomSex()).type(EnumGenerator.generateRandomServiceType())
                                       .date(dov).region(region).priority(NumberRandomGenerator.generateLongValueWithRange(1, 10))
                                       .numsOfRequiredVolunteers(Long.valueOf(3))
                                       .address(StringRandomGenerator.generateRandomKoreansWithLength(Long.valueOf(30)))
                                       .build();
    }
    @Test
    public void registerSenior_이후_업데이트(){
        Activity activity = makeActivity();
        activityBasicService.save(activity);

        System.out.println("At first, Activity: " + activity);

        RegisterSeniorRequestDto registerSeniorRequestDto = makeRegisterSeniorRequestDto();
        seniorVolunteerAdvancedService.registerSenior(registerSeniorRequestDto);

        Activity newActivity = activityBasicService.findActivityByRegionAndDOV(region, dov);
        System.out.println("After register first, Activity: " + newActivity);
    }
    @Test
    public void updateVolunteer_이후_업데이트(){

    }
    @Test
    public void deleteVolunteer_이후_업데이트(){

    }



}
