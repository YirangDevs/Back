package com.api.yirang.seniors.application.basicService;

import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.seniors.domain.senior.model.Senior;
import com.api.yirang.seniors.domain.volunteerService.exception.VolunteerServiceNullException;
import com.api.yirang.seniors.domain.volunteerService.model.VolunteerService;
import com.api.yirang.seniors.repository.persistence.maria.VolunteerServiceDao;
import com.api.yirang.seniors.support.custom.ServiceType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class VolunteerServiceBasicService {

    private final VolunteerServiceDao volunteerServiceDao;

    public void save(VolunteerService volunteerService) {
        volunteerServiceDao.save(volunteerService);
    }

    // find
    public Collection<VolunteerService> findSortedVolunteerServiceInSeniors(Collection<Senior> seniors) {
        Collection<VolunteerService> volunteerServices = volunteerServiceDao.findSortedVolunteerServiceInSeniors(seniors);
        if (volunteerServices.size() == 0){
            throw new VolunteerServiceNullException();
        }
        return volunteerServices;
    }

    public Collection<VolunteerService> findSortedVolunteerServiceInSeniorsAfterNow(Collection<Senior> seniors) {
        Collection<VolunteerService> volunteerServices = volunteerServiceDao.findSortedVolunteerServiceInSeniorsAfterNow(seniors, LocalDateTime.now());
        if (volunteerServices.size() == 0){
            throw new VolunteerServiceNullException();
        }
        return volunteerServices;
    }

    public VolunteerService findVolunteerServiceByActivityAndSenior(Activity activity, Senior senior){
        return volunteerServiceDao.findVolunteerServiceByActivityAndSenior(activity, senior).orElseThrow(VolunteerServiceNullException::new);
    }

    public VolunteerService findById(Long volunteerServiceId){
        return volunteerServiceDao.findById(volunteerServiceId).orElseThrow(VolunteerServiceNullException::new);
    }

    //update
    public void replaceActivity(Long volunteerServiceId, Activity newActivity) {
        volunteerServiceDao.updateActivity(volunteerServiceId, newActivity);
    }

    public void updateVolunteerService(Long volunteerServiceId, Long priority, ServiceType serviceType, Long numsOfRequiredVolunteers) {
        volunteerServiceDao.updateWithPriorityAndServiceType(volunteerServiceId, priority, serviceType, numsOfRequiredVolunteers);
    }

    // delete
    public void delete(Long volunteerServiceId) {
        // 찾았는데 없다.
        VolunteerService volunteerService = findById(volunteerServiceId);
        // 삭제
        volunteerServiceDao.delete(volunteerService);
    }
    public void deleteAllWithActivity(Activity activity){
        volunteerServiceDao.deleteAllWithActivity(activity);
    }
    public void deleteAllWithSenior(Senior senior){
        volunteerServiceDao.deleteAllWithSenior(senior);
    }

    // exist method
    public boolean existsVolunteerServiceByActivityAndSenior(Activity activity, Senior senior){
        return volunteerServiceDao.existsVolunteerServiceByActivityAndSenior(activity, senior);
    }


    public List<VolunteerService> findVolunteerServicesByActivity(Activity activity) {
        List<VolunteerService> volunteerServices = volunteerServiceDao.findVolunteerServicesByActivity(activity);
        if (volunteerServices.size() == 0){
            throw new VolunteerServiceNullException();
        }
        return volunteerServices;
    }

    public List<Senior> getWorkSeniorsFromActivity(Activity activity){
        List<VolunteerService> volunteerServices = findVolunteerServicesByActivity(activity);
        return volunteerServices.stream()
                                .filter(e-> e.getServiceType().equals(ServiceType.SERVICE_WORK))
                                .map(VolunteerService::getSenior)
                                .collect(Collectors.toList());
    }
    public List<Senior> getTalkSeniorsFromActivity(Activity activity){
        List<VolunteerService> volunteerServices = findVolunteerServicesByActivity(activity);
        return volunteerServices.stream()
                                .filter(e -> e.getServiceType().equals(ServiceType.SERVICE_TALK))
                                .map(VolunteerService::getSenior)
                                .collect(Collectors.toList());
    }


}
