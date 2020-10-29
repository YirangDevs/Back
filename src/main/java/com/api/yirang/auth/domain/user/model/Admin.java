package com.api.yirang.auth.domain.user.model;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "admin")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "admin_number")
    private Long adminNumber;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "user_id"
    )
    private User user;

    @Builder
    public Admin(User user) {
        this.user = user;
    }
}
