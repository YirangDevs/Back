package com.api.yirang.auth.application.basicService;


import com.api.yirang.apply.application.ApplyBasicService;
import com.api.yirang.auth.domain.user.exceptions.VolunteerNullException;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.domain.user.model.Volunteer;
import com.api.yirang.auth.repository.persistence.maria.VolunteerDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class VolunteerBasicService {

    // DI Services
    private final ApplyBasicService applyBasicService;

    // DI dao
    private final VolunteerDao volunteerDao;


    public Volunteer save(User user){
        return volunteerDao.save(
                Volunteer.builder()
                         .user(user)
                         .build()
        );
    }
    public Volunteer findVolunteerByUserId(Long userId) {
        return volunteerDao.findVolunteerByUserId(userId).orElseThrow(VolunteerNullException::new);
    }

    public void deleteAll() {
        volunteerDao.deleteAll();
    }

    public void delete(User user) {
        Volunteer volunteer = findVolunteerByUserId(user.getUserId());

        /** TO-DO
         * 매칭 data 지우기
         */

        applyBasicService.deleteAllWithVolunteer(volunteer);
        volunteerDao.deleteByUser(user);
    }
}
