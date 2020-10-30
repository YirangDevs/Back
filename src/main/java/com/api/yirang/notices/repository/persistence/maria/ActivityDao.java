package com.api.yirang.notices.repository.persistence.maria;

import com.api.yirang.notices.domain.activity.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityDao extends JpaRepository<Activity, Long> {
}
