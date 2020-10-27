package com.api.yirang.auth.domain.user.model;


import com.api.yirang.common.domain.region.model.DistributionRegion;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

    @OneToMany(mappedBy = "admin")
    private Set<DistributionRegion> distributionRegions;

    @Builder
    public Admin(User user) {
        this.user = user;
        this.distributionRegions = new HashSet<>();
    }
}
