package com.api.yirang.seniors.repository.persistence.maria;


import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.seniors.domain.senior.model.Senior;
import com.api.yirang.seniors.domain.volunteerService.model.VolunteerService;
import com.api.yirang.seniors.support.custom.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface VolunteerServiceDao extends JpaRepository<VolunteerService, Long> {

    // find
    Collection<VolunteerService> findVolunteerServicesBySenior(Senior senior);

    @Transactional
    @Query("SELECT V " +
           "FROM VolunteerService V " +
           "WHERE V.senior IN :seniors " +
           "ORDER BY V.activity.dtov DESC, V.senior.seniorName ASC ")
    Collection<VolunteerService> findSortedVolunteerServiceInSeniors(Collection<Senior> seniors);

    @Transactional
    @Query("SELECT V " +
           "FROM VolunteerService V " +
           "WHERE V.senior IN :seniors " +
           "     AND V.activity.dtov >= :now " +
           "ORDER BY V.activity.dtov DESC, V.senior.seniorName ASC ")
    Collection<VolunteerService> findSortedVolunteerServiceInSeniorsAfterNow(Collection<Senior> seniors, LocalDateTime now);

    Optional<VolunteerService> findVolunteerServiceByActivityAndSenior(Activity activity, Senior senior);

    //update
    @Modifying
    @Transactional
    @Query(
            "UPDATE VolunteerService V " +
            "SET V.activity =:activity " +
            "WHERE V.volunteerServiceId =:volunteerServiceId"
    )
    void updateActivity(Long volunteerServiceId, Activity activity);

    @Modifying
    @Transactional
    @Query(
            "UPDATE VolunteerService V " +
            "SET V.priority =:priority, V.serviceType =:serviceType, V.numsOfRequiredVolunteers =:numsOfRequiredVolunteers " +
            "WHERE V.volunteerServiceId =:volunteerServiceId"
    )
    void updateWithPriorityAndServiceType(Long volunteerServiceId, Long priority, ServiceType serviceType, Long numsOfRequiredVolunteers);

    // delete
    @Modifying
    @Transactional
    @Override
    void delete(VolunteerService volunteerService);

    // exist
    boolean existsVolunteerServiceByActivityAndSenior(Activity activity, Senior senior);

    Collection<VolunteerService> findVolunteerServicesByActivity(Activity activity);

    @Transactional
    @Modifying
    @Query("DELETE FROM VolunteerService V " +
           "WHERE V.activity =:activity ")
    void deleteAllWithActivity(Activity activity);

    @Transactional
    @Modifying
    @Query("DELETE FROM VolunteerService V " +
           "WHERE V.senior =:senior ")
    void deleteAllWithSenior(Senior senior);


}
