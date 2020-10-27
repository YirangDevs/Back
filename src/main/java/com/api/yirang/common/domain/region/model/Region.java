package com.api.yirang.notices.domain.region.model;

import javax.persistence.*;

@Entity
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_id")
    private Long regionId;

    @Column(name = "region_name", nullable = false)
    private String regionName;
}
