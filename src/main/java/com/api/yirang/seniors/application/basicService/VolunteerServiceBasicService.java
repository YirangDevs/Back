package com.api.yirang.seniors.application.basicService;

import com.api.yirang.seniors.domain.volunteerService.model.VolunteerService;
import com.api.yirang.seniors.repository.persistence.maria.VolunteerServiceDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VolunteerServiceBasicService {

    private final VolunteerServiceDao volunteerServiceDao;

    public void save(VolunteerService volunteerService) {
        volunteerServiceDao.save(volunteerService);
    }
}
