package com.api.yirang.seniors.application.advancedService;

import com.api.yirang.auth.application.basicService.AdminService;
import com.api.yirang.common.support.custom.ValidCollection;
import com.api.yirang.common.support.time.TimeConverter;
import com.api.yirang.common.support.type.Region;
import com.api.yirang.common.support.type.Sex;
import com.api.yirang.notices.application.basicService.ActivityBasicService;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.seniors.application.basicService.SeniorBasicService;
import com.api.yirang.seniors.application.basicService.VolunteerServiceBasicService;
import com.api.yirang.seniors.domain.senior.converter.SeniorConverter;
import com.api.yirang.seniors.domain.senior.model.Senior;
import com.api.yirang.seniors.domain.volunteerService.converter.VolunteerServiceConverter;
import com.api.yirang.seniors.domain.volunteerService.exception.AlreadyExistedVolunteerService;
import com.api.yirang.seniors.domain.volunteerService.model.VolunteerService;
import com.api.yirang.seniors.presentation.dto.request.RegisterSeniorRequestDto;
import com.api.yirang.seniors.presentation.dto.request.RegisterTotalSeniorRequestDto;
import com.api.yirang.seniors.presentation.dto.response.SeniorResponseDto;
import com.api.yirang.seniors.support.custom.ServiceType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SeniorVolunteerAdvancedService {

    // DI Senior's features Services
    private final SeniorBasicService seniorBasicService;
    private final VolunteerServiceBasicService volunteerServiceBasicService;

    // DI other Basic features Services
    private final ActivityBasicService activityBasicService;
    private final AdminService adminService;

    /** Activity required people update 하기
     * Senior Register, update, delete 후에는 Activity의 NOR을 Update 해야함
     * 누가 주체인가? 어디에 있어야 하는가 에 대한 고민 해보기
     */

    // Create Method
    public void registerSenior(final RegisterSeniorRequestDto registerSeniorRequestDto) {
        System.out.println("[SeniorVolunteerAdvancedService]: registerSenior를 실행하겠습니다.");
        // region을 이용해 region 구하기
        final Region region = registerSeniorRequestDto.getRegion();

        // date와 region 이용해서 등록되어있던 Activity 구하기
        Activity activity = activityBasicService.findActivityByRegionAndDOV(region, registerSeniorRequestDto.getDate());

        // Senior 만들기 혹은 기존의 있는 Senior을 구하기
        String phone = registerSeniorRequestDto.getPhone();
        ServiceType serviceType = registerSeniorRequestDto.getType();
        Long priority = registerSeniorRequestDto.getPriority();
        Long numsOfRequiredVolunteers = registerSeniorRequestDto.getNumsOfRequiredVolunteers();

        // 저장이 안되어있으면 만들기
        if(!seniorBasicService.isExistByPhone(phone)){
            seniorBasicService.save(SeniorConverter.convertFromDtoToModel(registerSeniorRequestDto));
        }
        // phone으로 찾기
        Senior senior = seniorBasicService.findSeniorByPhone(phone);

        // Senior와 Activity가 같으면 중복된 Service이다
        if( volunteerServiceBasicService.existsVolunteerServiceByActivityAndSenior(activity, senior)){
            throw new AlreadyExistedVolunteerService();
        }

        // Service에 등록하기
        VolunteerService volunteerService = VolunteerServiceConverter.convertToModel(serviceType, priority, activity,
                                                                                     senior, numsOfRequiredVolunteers);
        volunteerServiceBasicService.save(volunteerService);
    }

    // Check method
    public boolean checkSameDateAndSameRegion(ValidCollection<RegisterTotalSeniorRequestDto> registerTotalSeniorRequestDtos){
        System.out.println("[SeniorVolunteerAdvancedService]: checkSameDateAndSameRegion을 실행하겠습니다.");
        Set<Region> regions = new HashSet<>();
        Set<String> dates = new HashSet<>();

        registerTotalSeniorRequestDtos.getCollection()
                                            .forEach(e -> {
                                                regions.add(e.getRegion());
                                                dates.add(e.getDate());
                                            });
        return regions.size() == 1 && dates.size() == 1;
    }
    // Find methods
    public Collection<SeniorResponseDto> findSeniorsByRegion(final Region region) {
        System.out.println("[SeniorVolunteerAdvancedService]: findSeniorsByRegion를 실행하겠습니다.");

        // Region을 가지고 Seniors 찾기
        Collection<Senior> seniors = seniorBasicService.findSeniorsByRegion(region, false);
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
        Collection<Region> regions = adminService.findAreasByUserId(userId);

        // regions로 해당하는 Seniors 찾기
        Collection<Senior> seniors = new ArrayList<>();
        regions.stream().forEach(e -> seniors.addAll(seniorBasicService.findSeniorsByRegion(e, true)));


        // Seniors에 해당하는 봉사활동 이력 찾기
        Collection<VolunteerService> volunteerServices =
                volunteerServiceBasicService.findSortedVolunteerServiceInSeniors(seniors);

        return volunteerServices.stream()
                                .map(e -> VolunteerServiceConverter.convertFromModelToSeniorResponseDto(e))
                                .collect(Collectors.toList());
    }

    // Update
    public void updateVolunteerService(Long volunteerServiceId, final RegisterSeniorRequestDto registerSeniorRequestDto) {
        System.out.println("[SeniorVolunteerAdvancedService]: updateSenior를 실행하겠습니다.");

        // 기존의 volunteerSerivce를 불러오기
        VolunteerService existedVolunteerService = volunteerServiceBasicService.findById(volunteerServiceId);
        // 기존의 값과 비교하면서 update를 실행한다.
        Activity existedActivity = existedVolunteerService.getActivity();
        Senior existedSenior = existedVolunteerService.getSenior();

        String name = registerSeniorRequestDto.getName();
        Region region = registerSeniorRequestDto.getRegion();
        String address = registerSeniorRequestDto.getAddress();
        String phone = registerSeniorRequestDto.getPhone();
        Sex sex = registerSeniorRequestDto.getSex();
        Long numsOfRequiredVolunteers = registerSeniorRequestDto.getNumsOfRequiredVolunteers();

        // name 또는 region 또는 address, phone 또는 sex가 변했을 경우
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
            Activity newActivity = activityBasicService.findActivityByRegionAndDOV(region, dov);
            volunteerServiceBasicService.replaceActivity(volunteerServiceId, newActivity);
        }

        // type, priority 또는  numsOfRequiredVolunteers 가 변했을 경우
        Long priority = registerSeniorRequestDto.getPriority();
        ServiceType serviceType = registerSeniorRequestDto.getType();

        if (!existedVolunteerService.getPriority().equals(priority) ||
            existedVolunteerService.getServiceType() != serviceType ||
            existedVolunteerService.getNumsOfRequiredVolunteers() != numsOfRequiredVolunteers ){
            volunteerServiceBasicService.updateVolunteerService(volunteerServiceId, priority, serviceType, numsOfRequiredVolunteers);
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
