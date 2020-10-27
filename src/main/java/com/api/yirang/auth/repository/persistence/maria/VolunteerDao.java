package com.api.yirang.auth.repository.persistence.maria;


import com.api.yirang.auth.domain.user.model.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VolunteerDao extends JpaRepository<Volunteer, Long> {

}
