package com.api.yirang.auth.repository.persistence.maria;


import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.domain.user.model.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface VolunteerDao extends JpaRepository<Volunteer, Long> {

    @Transactional
    @Query("SELECT V " +
           "FROM Volunteer V " +
           "WHERE V.user.userId = :userId ")
    Optional<Volunteer> findVolunteerByUserId(Long userId);
}
