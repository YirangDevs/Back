package com.api.yirang.common.domain.region.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "region")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "region_id")
    private Long regionId;

    @Column(name = "region_name", nullable = false)
    private String regionName;

    @Builder
    public Region(String regionName) {
        this.regionName = regionName;
    }
}
