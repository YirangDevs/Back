package com.api.yirang.auth.repository.persistence.maria;

import com.api.yirang.auth.domain.user.model.Admin;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.common.support.type.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminDao extends JpaRepository<Admin, Long> {

    Optional<Admin> findAdminByAdminNumber(Long adminNumber);

    @Transactional
    @Query("SELECT A " +
           "FROM Admin A " +
           "WHERE A.user.userId =:userId")
    Optional<Admin> findAdminByUserId(Long userId);

    boolean existsAdminByUser(User user);

    @Modifying
    @Transactional
    @Query("DELETE FROM Admin AS a WHERE a.user =:user")
    void deleteByUser(User user);

    boolean existsAdminByUser_UserId(Long userId);

    @Modifying
    @Transactional
    @Query(
            "UPDATE Admin A " +
            "SET A.myAreas =:myAreas " +
            "WHERE A.user.userId =:userId"
    )
    void updateRegionsByUserId(Long userId, String myAreas);

    @Transactional
    Optional<Admin> findAdminByUser(User user);
}
