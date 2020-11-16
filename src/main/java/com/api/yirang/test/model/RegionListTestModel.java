package com.api.yirang.test.model;

import com.api.yirang.common.support.converter.RegionListConverter;
import com.api.yirang.common.support.type.Region;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@ToString(exclude = {"regions"})
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "region_list_test")
public class RegionListTestModel {

    @Id
    @Column
    private Long id;

    @Column
    @Getter(AccessLevel.NONE)
    private String regions;

    @Transient
    private List<Region> regionList;

    @Builder
    public RegionListTestModel(Long id) {
        this.id = id;
        this.regions = null;
        this.regionList = new ArrayList<>();
    }

    @PostLoad
    public void updateRegionListAfterLoad(){
        System.out.println("현재 Regions: " + this.regions);
        this.regionList = RegionListConverter.convertFromStringToList(this.regions);
    }

    @PrePersist
    @PreUpdate
    public void updateRegionsBeforePersist(){
        System.out.println("현재 RegionList: " + this.regionList);
        this.regions = RegionListConverter.convertFromListToString(this.regionList);
    }

}
