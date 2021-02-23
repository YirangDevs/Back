package com.api.yirang.email.repository;

import com.api.yirang.email.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {

    @Modifying
    @Query("UPDATE Email E " +
           "SET E.certificationNumbers =:certificationNumbers " +
           "WHERE E.user.userId =:userId")
    void updateEmailCertificationWithUserId(Long userId, String certificationNumbers);

    Optional<Email> findEmailByUser_UserId(Long userId);
}
