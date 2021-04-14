package com.api.yirang.matching.repository.maria;

import com.api.yirang.auth.domain.user.model.Volunteer;
import com.api.yirang.matching.model.maria.Matching;
import com.api.yirang.notices.domain.activity.model.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import java.util.List;

@Repository
public interface MatchingRepository extends JpaRepository<Matching, Long> {

    @Query("SELECT (COUNT (M) > 0) " +
           "FROM Matching M " +
           "WHERE M.volunteer.user.userId =:userId " +
           "     AND M.activity.dtov > :now ")
    @Transactional
    boolean existsMatchingByVolunteer_User_UserIdAndActivity_DtovAfterNow(Long userId, LocalDateTime now);

    @Query("SELECT M " +
           "FROM Matching M " +
           "WHERE M.volunteer.user.userId =:userId " +
           "    AND M.activity.dtov > :now ")
    @Transactional
    List<Matching> findMatchingsByVolunteer_User_UserIdAndActivity_DtovAfterNow(Long userId, LocalDateTime now);

    void deleteAllByVolunteer_User_UserId(Long userId);
    List<Matching> findMatchingsByActivity(Activity activity);

    List<Matching> findMatchingsByVolunteer_User_UserId(Long userId);

    void deleteAllByVolunteer(Volunteer volunteer);

    @Modifying
    void deleteAllByActivity(Activity activity);
}
