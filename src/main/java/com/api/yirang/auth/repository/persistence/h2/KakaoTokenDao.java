package com.api.yirang.auth.repository.persistence.h2;

import com.api.yirang.auth.domain.kakaoToken.model.KakaoToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface KakaoTokenDao extends JpaRepository<KakaoToken, Long> {

    boolean existsByUserId(long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM KakaoToken k WHERE k.userId=:userId")
    void deleteByUserId(@Param("userId") long userId);

    Optional<KakaoToken> findByUserId(long userId);
}
