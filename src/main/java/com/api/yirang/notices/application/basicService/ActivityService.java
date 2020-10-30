package com.api.yirang.notices.application.basicService;


import com.api.yirang.notices.repository.persistence.maria.ActivityDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityDao activityDao;

}
