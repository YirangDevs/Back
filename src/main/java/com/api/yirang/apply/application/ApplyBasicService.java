package com.api.yirang.apply.application;

import com.api.yirang.apply.domain.exception.AlreadyExistedApplyException;
import com.api.yirang.apply.domain.exception.ApplyNullException;
import com.api.yirang.apply.domain.model.Apply;
import com.api.yirang.apply.repository.persistence.maria.ApplyDao;
import com.api.yirang.auth.domain.user.model.Volunteer;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.seniors.support.custom.ServiceType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * Created by JeongminYoo on 2020/12/30
 * Work with Yirang
 * Email: likemin0142@gmail.com
 * Blog: http://Beewoom.github.io
 * Github: http://github.com/Biewoom
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ApplyBasicService {

    // DI field
    private final ApplyDao applyDao;

    public Apply save(Apply apply) {
        return applyDao.save(apply);
    }


    public Apply getUniqueApplyWithVolunteerAndActivity(Volunteer volunteer, Activity activity){
        return applyDao.findApplyByActivityAndVolunteer(activity, volunteer).orElseThrow(ApplyNullException::new);
    }


    public Collection<Apply> getAppliesFromActivity(Activity activity) {
        // null exception 제외
        Collection<Apply> applies = applyDao.findAppliesByActivity(activity);
        if (applies.size() == 0){
            throw new ApplyNullException();
        }
        return applies;
    }
    public Collection<Apply> getAppliesFromActivity(Activity activity, ServiceType serviceType) {
        // null exception 제외
        Collection<Apply> applies = applyDao.findAppliesByActivityAndServiceTypeOrderByDtoa(activity, serviceType);
        if (applies.size() == 0){
            throw new ApplyNullException();
        }
        return applies;
    }

    public Apply findApplyByApplyId(Long applyId) {
        return applyDao.findApplyByApplyId(applyId).orElseThrow(ApplyNullException::new);
    }

    public Collection<Apply> getAppliesFromVolunteer(Volunteer volunteer) {
        Collection<Apply> applies = applyDao.findAppliesByVolunteerOrdOrderByDtoa(volunteer);
        if(applies.size() == 0){
            throw new ApplyNullException();
        }
        return applies;
    }

    public void checkApplyByVolunteerAndActivity(Volunteer volunteer, Activity activity) {
        if (applyDao.existsApplyByVolunteerAndActivity(volunteer, activity)){
            throw new AlreadyExistedApplyException();
        }
    }

    public Boolean existApplyByVolunteerAndActivity(Volunteer volunteer, Activity activity){
        return applyDao.existsApplyByVolunteerAndActivity(volunteer, activity);
    }

    // Delete
    public void delete(Apply apply) {
        applyDao.delete(apply);
    }

    public void deleteAllWithVolunteer(Volunteer volunteer){
        applyDao.deleteAllWithVolunteer(volunteer);
    }
    public void deleteAllWithActivity(Activity activity){
        applyDao.deleteAllWithActivity(activity);
    }

    public void deleteAll() {
        applyDao.deleteAll();
    }
}
