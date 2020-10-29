package com.api.yirang.common.domain.region.model;


import com.api.yirang.auth.domain.user.model.Admin;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DistributionRegion that = (DistributionRegion) o;
        return Objects.equals(admin, that.admin) &&
               Objects.equals(region, that.region);
    }

    @Override
    public int hashCode() {
        return Objects.hash(admin, region);
    }
}
