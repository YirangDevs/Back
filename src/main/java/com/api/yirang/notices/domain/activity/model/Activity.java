package com.api.yirang.notices.domain.activity.model;


import com.api.yirang.common.support.type.Region;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.joda.time.LocalDateTime;

import javax.persistence.*;

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
    
    // 봉사자들의 신청 숫자!
    @Column(name = "numbers_of_apply")
    private Long noa;

    @Column(columnDefinition = "VARCHAR(3000)")
    private String content;

    @Column(name = "datetime_of_volunteer")
    private LocalDateTime dtov;

    @Column(name = "datetime_of_deadline")
    private LocalDateTime dtod;

    @Enumerated(EnumType.STRING)
    @Column(name = "region")
    private Region region;

    @Builder
    public Activity(Long nor, String content,
                    LocalDateTime dtov, LocalDateTime dtod,
                    Region region) {
        this.nor = nor;
        this.content = content;
        this.noa = Long.valueOf(0);
        this.dtov = dtov;
        this.dtod = dtod;
        this.region = region;
    }

}
