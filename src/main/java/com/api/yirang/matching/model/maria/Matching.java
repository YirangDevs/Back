package com.api.yirang.matching.model.maria;

import com.api.yirang.auth.domain.user.model.Volunteer;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.seniors.domain.senior.model.Senior;
import com.api.yirang.seniors.support.custom.ServiceType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "matching")
@Getter
@ToString
@NoArgsConstructor
public class Matching {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "matching_id")
    private Long matchingId;

    // 매칭된 시간
    @Column(name = "datetime_of_matching")
    private LocalDateTime dtom;

    @Column(name = "service_type")
    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "volunteer_number", foreignKey = @ForeignKey(name = "fk_volunteer_number_m"))
    private Volunteer volunteer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "activity_id", foreignKey = @ForeignKey(name = "fk_activity_id_m"))
    private Activity activity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "senior_id", foreignKey = @ForeignKey(name = "fk_senior_id_m"))
    private Senior senior;

    @Builder
    public Matching(Volunteer volunteer,
                    Activity activity, Senior senior){
        this.dtom = LocalDateTime.now(); // 객체를 만든 시간이 Matching 시간
        this.volunteer = volunteer;
        this.activity = activity;
        this.senior = senior;
    }
}
