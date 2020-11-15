package com.api.yirang.test.model;

import com.api.yirang.common.support.attributeConverter.RegionListAttributeConverter;
import com.api.yirang.common.support.type.Region;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@Entity
@Table(name = "region_list_test")
public class RegionListTest {

    @Id
    @Column
    private Long id;

    @Column
    @Convert(converter = RegionListAttributeConverter.class)
    private List<Region> regions;

    @Builder
    public RegionListTest(Long id) {
        this.id = id;
        this.regions = new ArrayList<>();
    }
}
