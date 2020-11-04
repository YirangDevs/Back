package com.api.yirang.seniors.application.advancedService;


import com.api.yirang.auth.application.intermediateService.AdminRegionService;
import com.api.yirang.common.domain.region.model.Region;
import com.api.yirang.common.service.RegionService;
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
import java.util.Collection;

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
        // region을 이용해 region 구하기
        Region region = regionService.findRegionByRegionName(registerSeniorRequestDto.getRegion());

        // date와 region 이용해서 Activity 구하기
        Activity activity = activityService.findActivityByRegionAndDOV(region, registerSeniorRequestDto.getDate());

        // Senior 만들기 혹은 기존의 있는 Senior을 구하기

        String phone = registerSeniorRequestDto.getPhone();
        ServiceType serviceType = registerSeniorRequestDto.getType();
        Long priority = registerSeniorRequestDto.getPriority();

        // 저장이 안되어있으면 만들기
        if(!seniorBasicService.isExistByPhone(phone)){
            seniorBasicService.save( SeniorConverter.convertFromDtoToModel(registerSeniorRequestDto, region));
        }
        // phone으로 찾기
        Senior senior = seniorBasicService.findSeniorByPhone(phone);

        // Service에 등록하기
        VolunteerService volunteerService = VolunteerServiceConverter.convertToModel(serviceType, priority,
                                                                                     activity, senior);
        volunteerServiceBasicService.save(volunteerService);
    }
    // Find methods
    public Collection<SeniorResponseDto> findSeniorsByRegion(String regionName) {
        // Region을 찾고
        Region region = regionService.findRegionByRegionName(regionName);

        // Region을 가지고 Seniors 찾기
        Collection<Senior> seniors = seniorBasicService.findSeniorsByRegion(region);
        // Seniors가 해당되는 봉사활동 이력 찾기
        Collection<VolunteerService> volunteerServices =
                volunteerServiceBasicService.findSortedVolunteerServiceInSeniors(seniors);

        return volunteerServices.stream().map();
    }

    public Collection<SeniorResponseDto> findSeniorsByMyArea(Long userId) {

        return null;
    }

    // Update

    public void updateSenior(Long seniorId, RegisterSeniorRequestDto registerSeniorRequestDto) {
    }

    // Delete
    public void deleteSenior(Long seniorId) {

    }
}
