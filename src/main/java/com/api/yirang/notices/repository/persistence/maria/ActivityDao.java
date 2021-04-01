package com.api.yirang.notices.repository.persistence.maria;

import com.api.yirang.common.support.type.Region;
import com.api.yirang.notices.domain.activity.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Collection;
import java.util.Optional;

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
    @Query("SELECT ( COUNT(A) > 0 ) " +
           "FROM Activity A " +
           "WHERE A.region = :region " +
           "      AND A.dtov > :startDtov " +
           "      AND A.dtov < :endDtov")
    boolean existsActivityByRegionAndRangeOfDTOV(Region region, LocalDateTime startDtov, LocalDateTime endDtov);

    @Transactional
    @Query("SELECT A " +
           "FROM Activity A " +
           "WHERE A.region =:region AND A.dtov =:dtov")
    Optional<Activity> findActivityByRegionAndDTOV(Region region, LocalDateTime dtov);

    List<Activity> findActivitiesByDtovBetween(LocalDateTime start, LocalDateTime end);


    @Transactional
    @Query("SELECT A " +
           "FROM Activity A " +
           "WHERE A.region =:region " +
           "      AND A.dtov > :startDtov " +
           "      AND A.dtov < :endDtov")
    Optional<Activity> findActivityByRegionAndRangeOfDTOV(Region region,
                                                          LocalDateTime startDtov, LocalDateTime endDtov);

    @Transactional
    @Query("SELECT A " +
            "FROM Activity A " +
            "WHERE A.region =:region " +
            "      AND A.dtov > :Dtov ")
    Collection<Activity> findActivityByRegionAndAfterDtov(Region region,
                                                          LocalDateTime Dtov);

    @Override
    Optional<Activity> findById(Long activityId);

    @Modifying
    @Transactional
    @Query("UPDATE Activity A " +
           "SET A.nor =:newNor, A.content =:newContent, " +
           "    A.dtov =:newDtov, A.dtod =:newDtod, " +
           "    A.region =:newRegion " +
           "WHERE A.activityId =:activityId ")
    void update(Long activityId, Long newNor, String newContent,
                LocalDateTime newDtov, LocalDateTime newDtod, Region newRegion);

    @Modifying
    @Transactional
    @Query("UPDATE Activity  A " +
           "SET A.noa =:newNoa " +
           "WHERE A.activityId =:activityId ")
    void updateNoa(Long activityId, Long newNoa);

    @Modifying
    @Transactional
    @Override
    void deleteById(Long activityId);

    @Modifying
    @Transactional
    @Query("UPDATE Activity A " +
           "SET A.nor = :numsOfRequiredVolunteers " +
           "WHERE A.activityId =:activityId")
    void updateWithNumsOfRequiredVolunteer(Long activityId, Long numsOfRequiredVolunteers);

}
