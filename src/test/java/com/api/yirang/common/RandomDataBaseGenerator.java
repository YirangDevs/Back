package com.api.yirang.common;

import com.api.yirang.auth.repository.persistence.maria.AdminDao;
import com.api.yirang.auth.repository.persistence.maria.VolunteerDao;
import com.api.yirang.email.repository.EmailRepository;
import com.api.yirang.matching.repository.maria.MatchingRepository;
import com.api.yirang.seniors.repository.persistence.maria.SeniorDao;
import lombok.Builder;

public class RandomDataBaseGenerator {

    private final SeniorDao seniorDao;
    private final AdminDao adminDao;
    private final EmailRepository emailRepository;
    private final VolunteerDao volunteerDao;
    private final MatchingRepository matchingRepository;

    @Builder
    public RandomDataBaseGenerator(SeniorDao seniorDao, AdminDao adminDao, EmailRepository emailRepository,
                                   VolunteerDao volunteerDao, MatchingRepository matchingRepository) {
        this.seniorDao = seniorDao;
        this.adminDao = adminDao;
        this.emailRepository = emailRepository;
        this.volunteerDao = volunteerDao;
        this.matchingRepository = matchingRepository;
    }
}
