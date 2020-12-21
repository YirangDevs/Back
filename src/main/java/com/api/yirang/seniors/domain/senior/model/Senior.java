package com.api.yirang.seniors.domain.senior.model;

import com.api.yirang.common.support.type.Region;
import com.api.yirang.common.support.type.Sex;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "senior")
@ToString
@NoArgsConstructor
@Getter
public class Senior {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "senior_id")
    private Long seniorId;

    // my field
    @Column(name = "senior_name")
    private String seniorName;

    @Column
    private Sex sex;

    @Column(columnDefinition = "VARCHAR(255)")
    private String address;

    @Column(columnDefinition = "VARCHAR(20)")
    private String phone;

    private Region region;

    @Builder
    public Senior(String seniorName, Sex sex, String address,
                  String phone, Region region) {
        this.seniorName = seniorName;
        this.sex = sex;
        this.address = address;
        this.phone = phone;
        this.region = region;
    }
}
