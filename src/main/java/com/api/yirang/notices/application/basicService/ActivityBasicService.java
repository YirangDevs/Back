package com.api.yirang.notices.application.basicService;


import com.api.yirang.apply.application.ApplyBasicService;
import com.api.yirang.apply.domain.exception.NoaNegativeException;
import com.api.yirang.auth.application.basicService.AdminService;
import com.api.yirang.auth.application.basicService.VolunteerBasicService;
import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.auth.support.type.Authority;
import com.api.yirang.common.support.time.TimeConverter;
import com.api.yirang.common.support.type.Region;
import com.api.yirang.matching.application.MatchingCrudService;
import com.api.yirang.matching.repository.maria.MatchingRepository;
import com.api.yirang.notices.domain.activity.converter.ActivityConverter;
import com.api.yirang.notices.domain.activity.exception.ActivityNullException;
import com.api.yirang.notices.domain.activity.exception.AlreadyExistedActivityException;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.notices.domain.notice.exception.NoticeNullException;
import com.api.yirang.notices.presentation.dto.ActivityOneResponseDto;
import com.api.yirang.notices.presentation.dto.ActivityResponseDto;
import com.api.yirang.notices.repository.persistence.maria.ActivityDao;
import com.api.yirang.notices.repository.persistence.maria.PageableActivityDao;
import com.api.yirang.seniors.application.basicService.VolunteerServiceBasicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class ActivityBasicService {

    private final ActivityDao activityDao;

    // DI Service
    private final ApplyBasicService applyBasicService;
    private final VolunteerServiceBasicService volunteerServiceBasicService;
    private final MatchingCrudService matchingCrudService;


    public Activity save(Activity activity) {
        // 있던 Activity가 중복되면 에러
        Region region = activity.getRegion();
        LocalDateTime dtov = activity.getDtov();
        if (activityDao.existsActivityByRegionAndDTOV(region, dtov)){
            throw new AlreadyExistedActivityException();
        }
        Activity returnedActivity = activityDao.save(activity);
        return returnedActivity;
    }

    public boolean existsActivityByRegionAndDov(Region region, String dov) {
        String startDtovStr = dov + " " + "00:00:00";
        String endDtovSTr = dov + " " + "23:59:59";

        LocalDateTime startDtov = TimeConverter.StringToLocalDateTime(startDtovStr);
        LocalDateTime endDtov = TimeConverter.StringToLocalDateTime(endDtovSTr);
        return activityDao.existsActivityByRegionAndRangeOfDTOV(region, startDtov, endDtov);
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
    public List<Activity> findAllActivityTomorrowAfterTomorrow(LocalDateTime now){

        LocalDateTime tomorrowStart = now.plusHours(33L);
        LocalDateTime tomorrowEnd = tomorrowStart.plusDays(1L);

        return activityDao.findActivitiesByDtovBetween(tomorrowStart, tomorrowEnd);
    }

    public List<Activity> findAllActivityTomorrow(LocalDateTime now) {

        LocalDateTime tomorrowStart = now.plusHours(11L);
        LocalDateTime tomorrowEnd = tomorrowStart.plusDays(1L);

        return activityDao.findActivitiesByDtovBetween(tomorrowStart, tomorrowEnd);
    }

    public ActivityOneResponseDto getOneActivityById(Long id){
        Activity activity = findActivityByActivityId(id);
        ActivityOneResponseDto activityOneResponseDto = ActivityConverter.ConvertOneActivityToDto(activity);
        return activityOneResponseDto;
    }



    public Long findNumsOfActivity(){
        System.out.println("[ActivityBasicService]: findNumsOfActivity를 실행하겠습니다.");
        return activityDao.count();
    }

    // update
    public void update(Long activityId, Activity toBeUpdatedActivity) {
        // (Noa:= apply 숫자는 바뀔 수 없음)
        Long newNor = toBeUpdatedActivity.getNor();

        String newContent = toBeUpdatedActivity.getContent();

        LocalDateTime newDtov = toBeUpdatedActivity.getDtov();
        LocalDateTime newDtod = toBeUpdatedActivity.getDtod();
        Region newRegion = toBeUpdatedActivity.getRegion();

        activityDao.update(activityId, newNor, newContent,
                           newDtov, newDtod, newRegion);
    }

    public void updateActivityWithRequiredVolunteer(Long activityId, Long numsOfRequiredVolunteers) {
        activityDao.updateWithNumsOfRequiredVolunteer(activityId, numsOfRequiredVolunteers);
    }

    public void deleteOnlyActivityById(Long activityId){
        activityDao.deleteById(activityId);
    }
    public void deleteAll(){
        activityDao.deleteAll();
    }

    public void deleteActivityById(Long activityId) {
        // 나중에 구현할 거지만 관련된 모든 걸 삭제하는 것
        Activity activity = findActivityByActivityId(activityId);
        // 1. Apply 삭제
        applyBasicService.deleteAllWithActivity(activity);
        // 2. volunteerService 삭제
        volunteerServiceBasicService.deleteAllWithActivity(activity);
        // 3. 매칭 삭제
        matchingCrudService.deleteAllWithActivity(activity);
        deleteOnlyActivityById(activityId);
    }

    // 봉사 신청자 수 늘리기
    public void addNumberOfApplicants(Activity activity, Long number) {
        Long activityId = activity.getActivityId();
        Long newNoa = activity.getNoa() + number; // 한 명 추가

        activityDao.updateNoa(activityId, newNoa);
    }

    public void addNumberOfApplicants(Activity activity) {
        addNumberOfApplicants(activity, Long.valueOf(1) );
    }

    // 봉사 신청자 수 줄이기

    public void subtractNumberOfApplicants(Activity activity, Long number){
        Long activityId = activity.getActivityId();
        Long newNoa = activity.getNoa() - number; // 한 명 추가

        // 0 보다 적으면 이상한 것
        if (newNoa < 0){
            throw new NoaNegativeException();
        }

        String content = activity.getContent();
        LocalDateTime dtov = activity.getDtov();
        LocalDateTime dtod = activity.getDtod();

        Region region = activity.getRegion();

        activityDao.updateNoa(activityId, newNoa);
    }

    public void subtractNumberOfApplicants(Activity activity) {
        subtractNumberOfApplicants(activity, Long.valueOf(1));
    }


}
