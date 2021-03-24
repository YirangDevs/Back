package com.api.yirang.apply.domain.model;

import com.api.yirang.apply.support.type.MatchingState;
import com.api.yirang.auth.domain.user.model.Volunteer;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.seniors.support.custom.ServiceType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Created by JeongminYoo on 2020/12/9
 * Work with Yirang
 * Email: likemin0142@gmail.com
 * Blog: http://Beewoom.github.io
 * Github: http://github.com/Biewoom
 */
@Entity
@Table(name = "apply")
@Getter
@ToString(exclude = {"volunteer", "activity"})
@NoArgsConstructor
public class Apply {

    // my fields
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "apply_id")
    private Long applyId;

    // 신청한 시간
    @Column(name = "datetime_of_apply")
    private LocalDateTime dtoa;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_type")
    @NotNull
    private ServiceType serviceType;

    @Enumerated(EnumType.STRING)
    @Column(name = "matching_state")
    private MatchingState matchingState;

    // relationships
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "volunteer_number", foreignKey = @ForeignKey(name = "fk_volunteer_number"))
    private Volunteer volunteer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "activty_id", foreignKey = @ForeignKey(name = "fk_activity_id"))
    private Activity activity;

    @Builder
    public Apply(@NotNull ServiceType serviceType,
                 Volunteer volunteer, Activity activity) {
        this.dtoa = LocalDateTime.now(); // 객체를 만든 시간이 Apply 시간
        this.matchingState = MatchingState.MATCHING_READY; // 처음 만들면 Ready인 상태
        this.serviceType = serviceType;
        this.volunteer = volunteer;
        this.activity = activity;
    }
}
