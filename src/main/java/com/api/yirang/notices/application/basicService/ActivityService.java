package com.api.yirang.notices.application.basicService;


import com.api.yirang.common.domain.region.model.Region;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.notices.repository.persistence.maria.ActivityDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityDao activityDao;

    @Transactional
    public void save(Activity activity) {
        // 있던 Activity가 중복되면 저장안함
        Region region = activity.getRegion();
        LocalDateTime dtov = activity.getDtov();
        if (!activityDao.existsActivityByRegionAndDTOV(region, dtov)){
            activityDao.save(activity);
        }
    }
}
