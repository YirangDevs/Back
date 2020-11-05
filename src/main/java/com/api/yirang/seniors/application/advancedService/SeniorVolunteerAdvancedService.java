package com.api.yirang.seniors.application.advancedService;


import com.api.yirang.auth.application.intermediateService.AdminRegionService;
import com.api.yirang.common.domain.region.model.Region;
import com.api.yirang.common.service.RegionService;
import com.api.yirang.common.support.time.TimeConverter;
import com.api.yirang.common.support.type.Sex;
import com.api.yirang.notices.application.basicService.ActivityService;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.seniors.application.basicService.SeniorBasicService;
import com.api.yirang.seniors.application.basicService.VolunteerServiceBasicService;
import com.api.yirang.seniors.domain.senior.converter.SeniorConverter;
import com.api.yirang.seniors.domain.senior.model.Senior;
import com.api.yirang.seniors.domain.volunteerService.converter.VolunteerServiceConverter;
import com.api.yirang.seniors.domain.volunteerService.model.VolunteerService;
import com.api.yirang.seniors.presentation.dto.request.RegisterSeniorRequestDto;
import com.api.yirang.seniors.presentation.dto.response.SeniorResponseDto;
import com.api.yirang.seniors.support.custom.ServiceType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SeniorVolunteerAdvancedService {

    // DI Senior's features Service
    private final SeniorBasicService seniorBasicService;
    private final VolunteerServiceBasicService volunteerServiceBasicService;

    // DI other feathures Service
    private final RegionService regionService;
    private final ActivityService activityService;
    private final AdminRegionService adminRegionService;

    // Create Method
    public void registerSenior(@Valid RegisterSeniorRequestDto registerSeniorRequestDto) {
        System.out.println("[SeniorVolunteerAdvancedService]: registerSenior를 실행하겠습니다.");
        // region을 이용해 region 구하기
        Region region = regionService.findRegionByRegionName(registerSeniorRequestDto.getRegion());

        // date와 region 이용해서 등록되어있던 Activity 구하기
        Activity activity = activityService.findActivityByRegionAndDOV(region, registerSeniorRequestDto.getDate());

        // Senior 만들기 혹은 기존의 있는 Senior을 구하기
        String phone = registerSeniorRequestDto.getPhone();
        ServiceType serviceType = registerSeniorRequestDto.getType();
        Long priority = registerSeniorRequestDto.getPriority();

        // 저장이 안되어있으면 만들기
        if(!seniorBasicService.isExistByPhone(phone)){
            seniorBasicService.save(SeniorConverter.convertFromDtoToModel(registerSeniorRequestDto, region));
        }
        // phone으로 찾기
        Senior senior = seniorBasicService.findSeniorByPhone(phone);

        // Service에 등록하기
        VolunteerService volunteerService = VolunteerServiceConverter.convertToModel(serviceType, priority, activity, senior);
        volunteerServiceBasicService.save(volunteerService);
    }
    // Find methods
    public Collection<SeniorResponseDto> findSeniorsByRegion(String regionName) {
        System.out.println("[SeniorVolunteerAdvancedService]: findSeniorsByRegion를 실행하겠습니다.");
        // Region을 찾고
        Region region = regionService.findRegionByRegionName(regionName);

        // Region을 가지고 Seniors 찾기
        Collection<Senior> seniors = seniorBasicService.findSeniorsByRegion(region);
        // Seniors가 해당되는 봉사활동 이력 찾기
        Collection<VolunteerService> volunteerServices =
                volunteerServiceBasicService.findSortedVolunteerServiceInSeniors(seniors);

        return volunteerServices.stream()
                                .map(e -> VolunteerServiceConverter.convertFromModelToSeniorResponseDto(e))
                                .collect(Collectors.toList());
    }

    public Collection<SeniorResponseDto> findSeniorsByMyArea(Long userId) {
        System.out.println("[SeniorVolunteerAdvancedService]: findSeniorsByMyArea를 실행하겠습니다.");
        // 해당하는 지역 찾기
        Collection<String> regionNames = adminRegionService.getMyRegions(userId);
        Collection<Region> regions = regionNames.stream()
                                                .map(e -> regionService.findRegionByRegionName(e))
                                                .collect(Collectors.toList());

        // regions로 해당하는 Seniors 찾기
        Collection<Senior> seniors = new ArrayList<>();
        regions.stream().forEach(e -> seniors.addAll(seniorBasicService.findSeniorsByRegion(e)));


        // Seniors에 해당하는 봉사활동 이력 찾기
        Collection<VolunteerService> volunteerServices =
                volunteerServiceBasicService.findSortedVolunteerServiceInSeniors(seniors);

        return volunteerServices.stream()
                                .map(e -> VolunteerServiceConverter.convertFromModelToSeniorResponseDto(e))
                                .collect(Collectors.toList());
    }

    // Update
    public void updateVolunteerService(Long volunteerServiceId, RegisterSeniorRequestDto registerSeniorRequestDto) {
        System.out.println("[SeniorVolunteerAdvancedService]: updateSenior를 실행하겠습니다.");

        // 기존의 volunteerSerivce를 불러오기
        VolunteerService volunteerService = volunteerServiceBasicService.findById(volunteerServiceId);
        // 기존의 값과 비교하면서 update를 실행한다.
        Activity existedActivity = volunteerService.getActivity();
        Senior existedSenior = volunteerService.getSenior();

        // name 또는 region 또는 address, phone 또는 sex가 변했을 경우
        String name = registerSeniorRequestDto.getName();
        Region region = regionService.findRegionByRegionName(registerSeniorRequestDto.getRegion() );
        String address = registerSeniorRequestDto.getAddress();
        String phone = registerSeniorRequestDto.getPhone();
        Sex sex = registerSeniorRequestDto.getSex();

        if (!existedSenior.getSeniorName().equals(name) || !existedSenior.getAddress().equals(address) ||
            !existedSenior.getPhone().equals(phone) || existedSenior.getRegion() != region ||
            existedSenior.getSex() != sex ){
            seniorBasicService.updateSenior(existedSenior, name, sex,
                                            address, phone, region);
        }

        // Date가 변했을 경우
        String existedDate = TimeConverter.LocalDateTimeToString(existedActivity.getDtov()).split(" ")[0];
        String dov = registerSeniorRequestDto.getDate();
        if (!existedDate.equals(dov)){
            Activity newActivity = activityService.findActivityByRegionAndDOV(region, dov);
            volunteerServiceBasicService.replaceActivity(volunteerServiceId, newActivity);
        }

        // type 또는 Priority가 변했을 경우
        Long priority = registerSeniorRequestDto.getPriority();
        ServiceType serviceType = registerSeniorRequestDto.getType();

        if (!volunteerService.getPriority().equals(priority) ||
            volunteerService.getServiceType() != serviceType){
            volunteerServiceBasicService.updateVolunteerService(volunteerServiceId, priority, serviceType);
        }

    }

    // Delete

    /**
     *  활동이력만 삭제하지, Senior 정보나 봉사활동 정보는 이상이 없다.
     */
    public void deleteVolunteerService(Long volunteerServiceId) {
        System.out.println("[SeniorVolunteerAdvancedService]: deleteSenior를 실행하겠습니다.");
        volunteerServiceBasicService.delete(volunteerServiceId);
    }
}
