package com.api.yirang.auth.repository.persistence.h2;

import com.api.yirang.auth.domain.kakaoToken.model.KakaoToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KakaoTokenDao extends JpaRepository<KakaoToken, Long> {

    boolean existsKakaoTokenByUserId(long userId);
    void deleteByUserId(long userId);
    Optional<KakaoToken> findByUserId(long userId);
}
