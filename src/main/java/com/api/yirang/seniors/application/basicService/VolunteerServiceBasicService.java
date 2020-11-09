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

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Transactional
public class VolunteerServiceBasicService {

    private final VolunteerServiceDao volunteerServiceDao;

    public void save(VolunteerService volunteerService) {
        volunteerServiceDao.save(volunteerService);
    }

    public Collection<VolunteerService> findSortedVolunteerServiceInSeniors(Collection<Senior> seniors) {
        Collection<VolunteerService> volunteerServices = volunteerServiceDao.findSortedVolunteerServiceInSeniors(seniors);
        if (volunteerServices.size() == 0) throw new VolunteerServiceNullException();
        return volunteerServices;
    }

    public VolunteerService findById(Long volunteerServiceId){
        return volunteerServiceDao.findById(volunteerServiceId).orElseThrow(VolunteerServiceNullException::new);
    }

    //update
    public void replaceActivity(Long volunteerServiceId, Activity newActivity) {
        volunteerServiceDao.updateActivity(volunteerServiceId, newActivity);
    }

    public void updateVolunteerService(Long volunteerServiceId, Long priority, ServiceType serviceType) {
        volunteerServiceDao.updateWithPriorityAndServiceType(volunteerServiceId, priority, serviceType);
    }

    public void delete(Long volunteerServiceId) {
        // 찾았는데 없다.
        VolunteerService volunteerService = findById(volunteerServiceId);
        // 삭제
        volunteerServiceDao.delete(volunteerService);
    }



}
