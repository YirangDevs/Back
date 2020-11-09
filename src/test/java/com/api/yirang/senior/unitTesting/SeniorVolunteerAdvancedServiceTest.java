package com.api.yirang.senior.unitTesting;

import com.api.yirang.auth.application.basicService.VolunteerBasicService;
import com.api.yirang.auth.application.intermediateService.AdminRegionService;
import com.api.yirang.auth.domain.user.model.Volunteer;
import com.api.yirang.common.domain.region.model.Region;
import com.api.yirang.common.generator.RegionGenerator;
import com.api.yirang.common.service.RegionService;
import com.api.yirang.common.support.time.TimeConverter;
import com.api.yirang.common.support.type.Sex;
import com.api.yirang.notice.generator.ActivityGenerator;
import com.api.yirang.notices.application.basicService.ActivityService;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.senior.generator.SeniorGenerator;
import com.api.yirang.senior.generator.VolunteerServiceGenerator;
import com.api.yirang.seniors.application.advancedService.SeniorVolunteerAdvancedService;
import com.api.yirang.seniors.application.basicService.SeniorBasicService;
import com.api.yirang.seniors.application.basicService.VolunteerServiceBasicService;
import com.api.yirang.seniors.domain.senior.model.Senior;
import com.api.yirang.seniors.domain.volunteerService.model.VolunteerService;
import com.api.yirang.seniors.presentation.dto.request.RegisterSeniorRequestDto;
import com.api.yirang.seniors.presentation.dto.response.SeniorResponseDto;
import com.api.yirang.seniors.support.custom.ServiceType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SeniorVolunteerAdvancedServiceTest {

    // Test할 Clas
    @InjectMocks
    SeniorVolunteerAdvancedService seniorVolunteerAdvancedService;

    @Mock
    SeniorBasicService seniorBasicService;

    @Mock
    VolunteerServiceBasicService volunteerServiceBasicService;

    @Mock
    RegionService regionService;

    @Mock
    ActivityService activityService;

    @Mock
    AdminRegionService adminRegionService;


    @Test
    public void 새로운_시니어_등록(){
        // 필요한 랜덤 모델 만들기
        Region region = RegionGenerator.generateRandomRegion();
        Activity activity = ActivityGenerator.createRandomActivity(region);
        Senior senior = SeniorGenerator.createRandomSenior(region);
        VolunteerService volunteerService = VolunteerServiceGenerator.createRandomVolunteerService(senior, activity);

        String name = senior.getSeniorName();
        String regionName = region.getRegionName();
        String address = senior.getAddress();
        String phone = senior.getPhone();
        Sex sex = senior.getSex();
        ServiceType serviceType = volunteerService.getServiceType();
        String date = TimeConverter.LocalDateTimeToString(activity.getDtov()).split(" ")[0];
        Long priority = volunteerService.getPriority();

        RegisterSeniorRequestDto registerSeniorRequestDto = RegisterSeniorRequestDto.builder()
                                                                                    .name(name).region(regionName)
                                                                                    .address(address).phone(phone)
                                                                                    .sex(sex).type(serviceType)
                                                                                    .date(date).priority(priority)
                                                                                    .build();
        System.out.println(registerSeniorRequestDto);

        when(regionService.findRegionByRegionName(regionName)).thenReturn(region);
        when(activityService.findActivityByRegionAndDOV(region, date)).thenReturn(activity);
        when(seniorBasicService.isExistByPhone(phone)).thenReturn(Boolean.FALSE);
        when(seniorBasicService.findSeniorByPhone(phone)).thenReturn(senior);

        seniorVolunteerAdvancedService.registerSenior(registerSeniorRequestDto);
    }

    @Test
    public void 지역으로_활동_이력_찾기(){
        // DB 모델 예시 설계
        String regionName = "중구";
        Region region = Region.builder().regionName(regionName).build();

        Senior firstSenior = SeniorGenerator.createRandomSenior(region);
        Senior secondSenior = SeniorGenerator.createRandomSenior(region);
        Senior thirdSenior = SeniorGenerator.createRandomSenior(region);

        Collection<Senior> seniors = Arrays.asList(firstSenior, secondSenior, thirdSenior);

        Activity firstActivity = ActivityGenerator.createRandomActivity(region);
        Activity secondActivity = ActivityGenerator.createRandomActivity(region);

        VolunteerService firstFirstVolunteerService =
                VolunteerServiceGenerator.createRandomVolunteerService(firstSenior, firstActivity);

        VolunteerService firstSecondVolunteerService =
                VolunteerServiceGenerator.createRandomVolunteerService(firstSenior, secondActivity);

        VolunteerService secondSecondVolunteerService =
                VolunteerServiceGenerator.createRandomVolunteerService(secondSenior, secondActivity);

        Collection<VolunteerService> volunteerServices = Arrays.asList(firstFirstVolunteerService, firstSecondVolunteerService, secondSecondVolunteerService)
                                                               .stream().sorted((lhs, rhs) ->
                                                                                {
                                                                                    if( lhs.getActivity().getDtov().equals(rhs.getActivity().getDtov()) )
                                                                                        return lhs.getSenior().getSeniorName().compareTo(rhs.getSenior().getSeniorName());
                                                                                    else
                                                                                        return ~lhs.getActivity().getDtov().compareTo(rhs.getActivity().getDtov());
                                                                                }).collect(Collectors.toList());

        when(regionService.findRegionByRegionName(regionName)).thenReturn(region);
        when(seniorBasicService.findSeniorsByRegion(region)).thenReturn(seniors);
        when(volunteerServiceBasicService.findSortedVolunteerServiceInSeniors(seniors)).thenReturn(volunteerServices);

        Collection<SeniorResponseDto> res = seniorVolunteerAdvancedService.findSeniorsByRegion(regionName);
        System.out.println(res);
    }


}
