package com.api.yirang.seniors.repository.persistence.maria;


import com.api.yirang.seniors.domain.volunteerService.model.VolunteerService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VolunteerServiceDao extends JpaRepository<VolunteerService, Long> {
}
