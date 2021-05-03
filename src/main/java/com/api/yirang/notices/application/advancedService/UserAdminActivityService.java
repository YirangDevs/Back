package com.api.yirang.notices.application.advancedService;

import com.api.yirang.apply.application.ApplyAdvancedService;
import com.api.yirang.apply.application.ApplyBasicService;
import com.api.yirang.apply.domain.model.Apply;
import com.api.yirang.auth.application.basicService.AdminService;
import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.support.type.Authority;
import com.api.yirang.common.support.type.Region;
import com.api.yirang.img.application.ImgService;
import com.api.yirang.notices.domain.activity.converter.ActivityConverter;
import com.api.yirang.notices.domain.activity.exception.ActivityNullException;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.notices.presentation.dto.ActivityAppliesDto;
import com.api.yirang.notices.presentation.dto.ActivityResponseDto;
import com.api.yirang.notices.presentation.dto.embeded.ActivityApplyDto;
import com.api.yirang.notices.repository.persistence.maria.ActivityDao;
import com.api.yirang.notices.repository.persistence.maria.PageableActivityDao;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserAdminActivityService {

    private final PageableActivityDao pageableActivityDao;

    private final ApplyBasicService applyBasicService;
    private final UserService userService;
    private final AdminService adminService;
    private final ActivityDao activityDao;
    private final ImgService imgService;

    public Collection<ActivityResponseDto> getAllActivityByPage(Integer page, Long userId){
        int pageNum = 14;

        Authority authority = userService.getAuthorityByUserId(userId);
        Pageable pageWithFourTeenElements = PageRequest.of(page, pageNum, Sort.by("dtov").descending());;
        Page<Activity> activityPage = pageableActivityDao.findAll(pageWithFourTeenElements);
        Collection<Activity> activity = activityPage.toList();
        Collection<ActivityResponseDto> activityResponseDtos = null;

        if (activity.size() == 0){
            throw new ActivityNullException();
        }
        switch (authority){
            case ROLE_ADMIN:
                List<Region> regions = adminService.findAreasByUserId(userId);
                activityResponseDtos = activity.stream().filter(e->regions.contains(e.getRegion()) && e.getDtov().isAfter(LocalDateTime.now()))
                                               .map(e->{
                                                   return ActivityConverter.ConvertActivityToDto(e);
                                               }).collect(Collectors.toList());
                break;
            case ROLE_SUPER_ADMIN:
                activityResponseDtos = activity.stream().map(e->{
                    return ActivityConverter.ConvertActivityToDto(e);
                }).collect(Collectors.toList());
                break;
            default:
                break;
        }

        return activityResponseDtos;
    }

    public Long getActivityNumByAuthority(Long userId){
        Authority authority = userService.getAuthorityByUserId(userId);
        Long count = 0L;
        switch(authority){
            case ROLE_ADMIN:
                List<Region> regions = adminService.findAreasByUserId(userId);
                LocalDateTime now = LocalDateTime.now();

                for(Region region : regions){
                    count += activityDao.findActivityByRegionAndAfterDtov(region, now).stream()
                                        .count();
                }
                break;
            case ROLE_SUPER_ADMIN:
                count += activityDao.count();
                break;

            default:
                break;
        }
        return count;
    }

    public ActivityAppliesDto getAllAppliersByActivityId(Long activityId) {
        // 1. activity 찾기
        Activity activity = activityDao.findById(activityId).orElseThrow(ActivityNullException::new);
        // 2. applies 얻음
        Collection<Apply> applies = applyBasicService.getAppliesFromActivity(activity);

        // 3. 바꿈
        List<ActivityApplyDto> activityApplyDtos = applies.stream()
                                                        .map(apply ->
                                                             {
                                                                 User user = apply.getVolunteer().getUser();
                                                                 return ActivityApplyDto.builder()
                                                                                 .userId(user.getUserId())
                                                                                 .realname(user.getRealname())
                                                                                 .email(user.getEmail())
                                                                                 .sex(user.getSex()).phone(user.getPhone())
                                                                                 .serviceType(apply.getServiceType())
                                                                                 .profileImg(imgService.getMyImgNullable(user.getUserId()))
                                                                                 .build();
                                                             })
                                                        .collect(Collectors.toList());

        return ActivityAppliesDto.builder()
                                 .appliers(activityApplyDtos)
                                 .build();
    }
}
