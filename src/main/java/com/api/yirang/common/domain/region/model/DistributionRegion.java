package com.api.yirang.common.domain.region.model;


import com.api.yirang.auth.domain.user.model.Admin;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "distribution_region")
@NoArgsConstructor
@Getter
@ToString
public class DistributionRegion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "distribution_id")
    private Long distributionId;

    @ManyToOne
    @JoinColumn(name = "admin_number", foreignKey = @ForeignKey(name = "fk_admin_distribution"))
    private Admin admin;

    @ManyToOne
    @JoinColumn(name = "region_id", foreignKey = @ForeignKey(name = "fk_region_distribution"))
    private Region region;

    @Builder
    public DistributionRegion(Admin admin, Region region) {
        this.admin = admin;
        this.region = region;
    }
}
