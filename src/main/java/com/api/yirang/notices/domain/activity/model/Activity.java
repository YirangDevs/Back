package com.api.yirang.notices.domain.activity.model;


import com.api.yirang.common.domain.region.model.Region;
import com.api.yirang.notices.domain.notice.model.Notice;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "activity")
@Getter
@ToString
@NoArgsConstructor
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "activity_id")
    private Long activityId;

    // My field
    @Column(name = "numbers_of_request")
    private Long nor;

    @Column(name = "numbers_of_apply")
    private Long noa;

    @Column(name = "date_of_volunteer")
    private LocalDateTime dov;

    @Column(name = "time_of_volunteer")
    private LocalDate


    // relationships
    @ManyToOne
    @JoinColumn(name = "region_id", foreignKey = @ForeignKey(name = "fk_activity_region"))
    private Region region;

    @Builder
    public Activity(Long nor, String content,
                    LocalDateTime dov, Region region) {
        this.nor = nor;
        this.noa = Long.valueOf(0);
        this.dov = dov;
        this.region = region;
    }
}
