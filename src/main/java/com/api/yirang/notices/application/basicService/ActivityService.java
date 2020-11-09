package com.api.yirang.notices.application.basicService;


import com.api.yirang.common.domain.region.model.Region;
import com.api.yirang.common.support.time.TimeConverter;
import com.api.yirang.notices.domain.activity.exception.ActivityNullException;
import com.api.yirang.notices.domain.activity.exception.AlreadyExistedActivityException;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.notices.repository.persistence.maria.ActivityDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ActivityService {

    private final ActivityDao activityDao;

    public Long save(Activity activity) {
        // 있던 Activity가 중복되면 에러
        Region region = activity.getRegion();
        LocalDateTime dtov = activity.getDtov();
        if (activityDao.existsActivityByRegionAndDTOV(region, dtov)){
            throw new AlreadyExistedActivityException();
        }
        Activity returnedActivity = activityDao.save(activity);
        return returnedActivity.getActivityId();
    }

    public Activity findActivityByRegionAndDTOV(Region region, String dov, String tov){
        String dtovStr = dov + " " + tov;
        LocalDateTime dtov = TimeConverter.StringToLocalDateTime(dtovStr);
        return activityDao.findActivityByRegionAndDTOV(region, dtov).orElseThrow(ActivityNullException::new);
    }
    public Activity findActivityByRegionAndDOV(Region region, String dov){
        String startDtovStr = dov + " " + "00:00:00";
        String endDtovSTr = dov + " " + "23:59:59";

        LocalDateTime startDtov = TimeConverter.StringToLocalDateTime(startDtovStr);
        LocalDateTime endDtov = TimeConverter.StringToLocalDateTime(endDtovSTr);
        return activityDao.findActivityByRegionAndRangeOfDTOV(region, startDtov, endDtov).orElseThrow(ActivityNullException::new);
    }

    public Activity findActivityByActivityId(Long activityId){
        return activityDao.findById(activityId).orElseThrow(ActivityNullException::new);
    }

    public void update(Long activityId, Activity toBeUpdatedActivity) {
        // Acitivty 찾기
        Activity activity = findActivityByActivityId(activityId);

        // (Noa:= apply 숫자는 바뀔 수 없음)
        Long newNor = toBeUpdatedActivity.getNor();

        String newContent = toBeUpdatedActivity.getContent();

        LocalDateTime newDtov = toBeUpdatedActivity.getDtov();
        LocalDateTime newDtod = toBeUpdatedActivity.getDtod();
        Region newRegion = toBeUpdatedActivity.getRegion();

        activityDao.update(activityId, newNor, newContent,
                           newDtov, newDtod, newRegion);
    }

    public void deleteOnlyActivityById(Long activityId){
        activityDao.deleteById(activityId);
    }
    public void deleteAll(){
        activityDao.deleteAll();
    }

    public boolean isExistedActivityById(Long activityId){
        return activityDao.existsById(activityId);
    }

    public void deleteActivityById(Long activityId) {
        // 나중에 구현할 거지만 관련된 모든 걸 삭제하는 것
        // 봉사 매칭, 신청, service, notice 등
        // 지금은 그냥 삭제랑 같음
        deleteOnlyActivityById(activityId);
    }
}
