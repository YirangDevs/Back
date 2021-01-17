package com.api.yirang.auth.domain.user.model;

import com.api.yirang.auth.support.type.Authority;
import com.api.yirang.common.support.type.Sex;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "user")
public class User {

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column
    private String username; // Base64로 인코딩 한 값

    @Enumerated(EnumType.STRING)
    @Column
    private Sex sex;

    @Column(columnDefinition = "VARCHAR(255) default 'unknown'")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column
    private Authority authority;

    @Builder
    public User(Long userId, String username,
                Sex sex, String email, Authority authority) {
        this.userId = userId;
        this.username = username;
        this.sex = sex;
        this.email = email;
        this.authority = authority;
    }

}
