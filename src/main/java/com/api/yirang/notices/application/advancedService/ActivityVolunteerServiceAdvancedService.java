package com.api.yirang.notices.application.advancedService;

import com.api.yirang.notices.application.basicService.ActivityBasicService;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.seniors.application.basicService.VolunteerServiceBasicService;
import com.api.yirang.seniors.domain.volunteerService.model.VolunteerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * Created by JeongminYoo on 2020/11/30
 * Work with Yirang
 * Email: likemin0142@gmail.com
 * Blog: http://Beewoom.github.io
 * Github: http://github.com/Biewoom
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ActivityVolunteerServiceAdvancedService {

    private final ActivityBasicService activityBasicService;
    private final VolunteerServiceBasicService volunteerServiceBasicService;


    // util class

    public void updateActivityNOR(Activity activity) {

        // Activity에 해당하는 VolunteerService 들을 구하기
        Collection<VolunteerService> volunteerServices = volunteerServiceBasicService.findVolunteerServicesByActivity(activity);
        Long numsOfRequiredVolunteers = volunteerServices.stream().mapToLong(e -> e.getNumsOfRequiredVolunteers()).sum();

        Long activityId = activity.getActivityId();
        activityBasicService.updateActivityWithRequiredVolunteer(activityId, numsOfRequiredVolunteers);
    }
}
