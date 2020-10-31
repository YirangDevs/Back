package com.api.yirang.notices.repository.persistence.maria;

import com.api.yirang.common.domain.region.model.Region;
import com.api.yirang.notices.domain.activity.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface ActivityDao extends JpaRepository<Activity, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Activity A")
    @Override
    void deleteAll();

    @Transactional
    @Query("SELECT ( COUNT(A) > 0  ) " +
           "FROM Activity A " +
           "WHERE A.region = :region AND A.dtov = :dtov")
    boolean existsActivityByRegionAndDTOV(Region region, LocalDateTime dtov);

    @Transactional
    @Query("SELECT A " +
           "FROM Activity A " +
           "WHERE A.region = :region AND A.dtov =:dtov")
    Activity findActivityByRegionAndDTOV(Region region, LocalDateTime dtov);
}
