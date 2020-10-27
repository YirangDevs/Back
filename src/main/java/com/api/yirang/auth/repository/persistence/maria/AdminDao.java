package com.api.yirang.auth.repository.persistence.maria;

import com.api.yirang.auth.domain.user.model.Admin;
import com.api.yirang.auth.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AdminDao extends JpaRepository<Admin, Long> {

    Optional<Admin> findAdminByAdminNumber(Long adminNumber);

    @Transactional
    @Query("SELECT a FROM Admin AS a WHERE a.user.userId=:userId")
    Optional<Admin> findAdminByUserId(Long userId);

    boolean findAdminByUser(User user);
}
