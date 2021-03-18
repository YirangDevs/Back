package com.api.yirang.email.repository;

import com.api.yirang.email.model.Email;
import com.api.yirang.email.util.Validation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Email E " +
           "SET E.certificationNumbers =:certificationNumbers " +
           "WHERE E.user.userId =:userId")
    void updateEmailCertificationWithUserId(Long userId, String certificationNumbers);

    @Modifying
    @Transactional
    @Query("UPDATE Email E " +
           "SET E.validation =:validation " +
           "WHERE E.user.userId =:userId")
    void updateEmailVerificationWithUserId(Long userId, Validation validation);

    Optional<Email> findEmailByUser_UserId(Long userId);

}
