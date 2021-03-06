package com.api.yirang.auth.domain.user.model;

import com.api.yirang.auth.support.type.Authority;
import com.api.yirang.common.support.type.Region;
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

    @Column
    private String phone;

    @Column
    private String realname;

    @Column(columnDefinition = "VARCHAR(255) default 'unknown'")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column
    private Authority authority;

    @Enumerated(EnumType.STRING)
    @Column
    private Region firstRegion;

    @Enumerated(EnumType.STRING)
    @Column
    private Region secondRegion;

    @Builder
    public User(Long userId, String username, String realname, String phone,
                Region firstRegion, Region secondRegion,
                Sex sex, String email, Authority authority) {
        this.userId = userId;
        this.username = username;
        this.sex = sex;
        this.email = email;
        this.authority = authority;
        this.phone = phone;
        this.realname = realname;
        this.firstRegion = firstRegion;
        this.secondRegion = secondRegion;
    }

}
