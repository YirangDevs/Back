package com.api.yirang.auth.domain.user.model;

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


    @Column(columnDefinition = "VARCHAR(255) default 'USER'")
    private String authority;

    @Builder
    public User(Long id, Long userId, String authority) {
        this.id = id;
        this.userId = userId;
        this.authority = authority;
    }
}
