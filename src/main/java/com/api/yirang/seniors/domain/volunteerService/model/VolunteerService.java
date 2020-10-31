package com.api.yirang.seniors.domain.volunteerService.model;


import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.seniors.domain.senior.model.Senior;
import com.api.yirang.seniors.support.custom.ServiceType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import javax.persistence.*;

@Entity
@Table(name = "volunteer_service")
@NoArgsConstructor
@Getter
@ToString
public class VolunteerService {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "volunteer_service_id")
    private Long volunteerServiceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_type")
    private ServiceType serviceType;

    @Column
    private Long priority;

    // relationships
    @ManyToOne
    @JoinColumn(name = "activity_id", foreignKey = @ForeignKey(name = "fk_activity_service"))
    private Activity activity;

    @ManyToOne
    @JoinColumn(name = "senior_id", foreignKey = @ForeignKey(name = "fk_senior_service"))
    private Senior senior;

    @Builder
    public VolunteerService(ServiceType serviceType, Long priority) {
        this.serviceType = serviceType;
        this.priority = priority;
    }
}
