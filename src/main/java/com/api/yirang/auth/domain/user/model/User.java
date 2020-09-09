package com.api.yirang.auth.domain.user.model;

import com.api.yirang.auth.support.type.Authority;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column
    private String username;

    @Column
    private String fileUrl;

    @Column(columnDefinition = "VARCHAR(255) default 'unknown'")
    private String sex;

    @Column(columnDefinition = "VARCHAR(255) default 'unknown'")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column
    private Authority authority;

    @Builder
    public User(Long userId, String username, String fileUrl,
                String sex, String email, Authority authority) {
        this.userId = userId;
        this.username = username;
        this.fileUrl = fileUrl;
        this.sex = sex;
        this.email = email;
        this.authority = authority;
    }

    @Override
    public String toString() {
        return "User{" +
               "id=" + id +
               ", userId=" + userId +
               ", username='" + username + '\'' +
               ", fileUrl='" + fileUrl + '\'' +
               ", sex='" + sex + '\'' +
               ", email='" + email + '\'' +
               ", authority=" + authority +
               '}';
    }
}
