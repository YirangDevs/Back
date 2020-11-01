package com.api.yirang.notices.domain.notice.model;

import com.api.yirang.auth.domain.user.model.Admin;
import com.api.yirang.notices.domain.activity.model.Activity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "notice")
@Getter
@ToString
@NoArgsConstructor
public class Notice {

    // my fields
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "notice_id")
    private Long noticeId;
    
    @Column
    @NotBlank
    private String title;

    @Column(name = "datetime_of_written")
    private LocalDateTime dtow;

    // relationships
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "admin_number", foreignKey = @ForeignKey(name = "fk_admin_notice"))
    private Admin admin;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "activity_id", foreignKey = @ForeignKey(name = "fk_activity_notice"))
    private Activity activity;

    // Constructors
    @Builder
    public Notice(String title,
                  Admin admin, Activity activity) {
        this.title = title;
        this.dtow = LocalDateTime.now(); // 객체 만든 시간이 writtien 시간
        this.admin = admin;
        this.activity = activity;
    }
}
