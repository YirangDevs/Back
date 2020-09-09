package com.api.yirang.auth.domain.kakaoToken.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class KakaoToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column
    private String kakaoAccessToken;

    @Column
    private String kakaoRefreshToken;

    @Column
    private LocalDateTime kakaoRefreshExpiredTime;

    @Builder
    public KakaoToken(Long userId, String kakaoAccessToken, String kakaoRefreshToken, LocalDateTime kakaoRefreshExpiredTime) {
        this.userId = userId;
        this.kakaoAccessToken = kakaoAccessToken;
        this.kakaoRefreshToken = kakaoRefreshToken;
        this.kakaoRefreshExpiredTime = kakaoRefreshExpiredTime;
    }

    @Override
    public String toString() {
        return "KakaoToken{" +
               "id=" + id +
               ", userId=" + userId +
               ", kakaoAccessToken='" + kakaoAccessToken + '\'' +
               ", kakaoRefreshToken='" + kakaoRefreshToken + '\'' +
               ", kakaoRefreshExpiredTime=" + kakaoRefreshExpiredTime +
               '}';
    }

}
