package com.api.yirang.seniors.application.basicService;

import com.api.yirang.seniors.repository.persistence.maria.VolunteerServiceDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VolunteerBasicService {

    private final VolunteerServiceDao volunteerServiceDao;
}
