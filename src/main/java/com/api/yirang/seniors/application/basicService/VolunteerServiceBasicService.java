package com.api.yirang.seniors.application.basicService;

import com.api.yirang.seniors.domain.senior.model.Senior;
import com.api.yirang.seniors.domain.volunteerService.exception.VolunteerServiceNullException;
import com.api.yirang.seniors.domain.volunteerService.model.VolunteerService;
import com.api.yirang.seniors.repository.persistence.maria.VolunteerServiceDao;
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

    public Collection<? extends VolunteerService> findVolunteerServicesBySenior(Senior senior) {
        Collection<VolunteerService> volunteerServices = volunteerServiceDao.findVolunteerServicesBySenior(senior);
        if (volunteerServices.size() == 0) throw new VolunteerServiceNullException();
        return volunteerServices;
    }
    public Collection<? extends VolunteerService> findNullableVolunteerServicesBySenior(Senior senior) {
        Collection<VolunteerService> volunteerServices = volunteerServiceDao.findVolunteerServicesBySenior(senior);
        return volunteerServices;
    }

    public Collection<VolunteerService> findSortedVolunteerServiceInSeniors(Collection<Senior> seniors) {
        Collection<VolunteerService> volunteerServices = volunteerServiceDao.findSortedVolunteerServiceInSeniors(seniors);
        if (volunteerServices.size() == 0) throw new VolunteerServiceNullException();
        return volunteerServices;
    }

}
