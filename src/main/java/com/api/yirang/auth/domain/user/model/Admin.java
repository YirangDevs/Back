package com.api.yirang.auth.domain.user.model;


import com.api.yirang.common.support.converter.RegionListConverter;
import com.api.yirang.common.support.type.Region;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString(exclude = {"user", "myAreas"})
@Entity
@Table(name = "admin")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "admin_number")
    private Long adminNumber;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "my_areas")
    @Getter(AccessLevel.NONE)
    private String myAreas;

    @Transient
    @Setter
    private List<Region> regions;

    @Builder
    public Admin(User user, List<Region> regions) {
        this.user = user;
        this.myAreas = null;
        this.regions = regions;
    }

    protected Admin() {
        this.user = null;
        this.myAreas = null;
        this.regions = new ArrayList<>();
    }

    @PostLoad
    public void updateRegionsAfterLoad(){
        this.regions = RegionListConverter.convertFromStringToList(this.myAreas);
    }
    @PrePersist
    @PreUpdate
    public void updateMyAreasBeforePersist(){
        this.myAreas = RegionListConverter.convertFromListToString(this.regions);
    }
}
