package com.api.yirang.auth.application.basicService;


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

    // DI dao
    private final VolunteerDao volunteerDao;

    public void save(User user){
        volunteerDao.save(
                Volunteer.builder()
                         .user(user)
                         .build()
        );
    }
    public Volunteer findVolunteerByUserId(Long userId) {
        return volunteerDao.findVolunteerByUserId(userId).orElseThrow(VolunteerNullException::new);
    }
}
