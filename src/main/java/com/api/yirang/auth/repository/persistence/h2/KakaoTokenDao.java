package com.api.yirang.auth.repository.persistence.h2;

import com.api.yirang.auth.domain.kakaoToken.model.KakaoToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KakaoTokenDao extends JpaRepository<KakaoToken, Long> {
    KakaoToken findByUserId(long userId);
}
