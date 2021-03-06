package com.api.yirang.apply.repository.persistence.maria;

import com.api.yirang.apply.domain.model.Apply;
import com.api.yirang.apply.support.type.MatchingState;
import com.api.yirang.auth.domain.user.model.Volunteer;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.seniors.support.custom.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

/**
 * Created by JeongminYoo on 2020/12/30
 * Work with Yirang
 * Email: likemin0142@gmail.com
 * Blog: http://Beewoom.github.io
 * Github: http://github.com/Biewoom
 */
@Repository
public interface ApplyDao extends JpaRepository<Apply, Long> {

    Optional<Apply> findApplyByActivityAndVolunteer(Activity activity, Volunteer volunteer);

    Collection<Apply> findAppliesByActivityAndServiceTypeOrderByDtoa(Activity activity, ServiceType serviceType);
    Collection<Apply> findAppliesByActivity(Activity activity);

    Collection<Apply> findAppliesByVolunteer(Volunteer volunteer);

    @Transactional
    @Query("SELECT A " +
           "FROM Apply A " +
           "WHERE A.volunteer =:volunteer " +
           "ORDER BY A.dtoa DESC ")
    Collection<Apply> findAppliesByVolunteerOrdOrderByDtoa(Volunteer volunteer);

    boolean existsApplyByVolunteerAndActivity(Volunteer volunteer, Activity activity);

    @Transactional
    @Query("SELECT ( COUNT(A) > 0) " +
           "FROM Apply A " +
           "WHERE A.volunteer.user.userId =:userId " +
           "      AND A.activity.dtov > :now")
    boolean existsApplyByVolunteer_User_UserIdAndActivity_DtovAfterNow(Long userId, LocalDateTime now);


    Optional<Apply> findApplyByApplyId(Long applyId);

    @Transactional
    @Modifying
    @Query("UPDATE Apply A " +
           "SET A.matchingState =:matchingState " +
           "WHERE A.applyId =:applyId")
    void updateMatchingStateByApplyId(Long applyId, MatchingState matchingState);

    @Transactional
    @Modifying
    @Query("DELETE FROM Apply A " +
           "WHERE A.volunteer =:volunteer ")
    void deleteAllWithVolunteer(Volunteer volunteer);

    @Transactional
    @Modifying
    @Query("DELETE FROM Apply A " +
           "WHERE A.activity =:activity ")
    void deleteAllWithActivity(Activity activity);

    void  deleteAllByVolunteer_User_UserId(Long userId);
}
