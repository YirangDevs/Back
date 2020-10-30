package com.api.yirang.notices.domain.notice.model;

import com.api.yirang.auth.domain.user.model.Admin;
import com.api.yirang.notices.domain.activity.model.Activity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notice")
@Getter
@NoArgsConstructor
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long noticeId;
    
    @Column
    private String title;

    @Column(columnDefinition = "VARCHAR(3000)")
    private String content;

    @Column(name = "day_of_deadline")
    private LocalDateTime dod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_number", foreignKey = @ForeignKey(name = "fk_admin_notice"))
    private Admin admin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", foreignKey = @ForeignKey(name = "fk_activity_notice"))
    private Activity activity;

    @Builder
    public Notice(Long noticeId, String title, String content,
                  LocalDateTime dod, Activity activity) {
        this.noticeId = noticeId;
        this.title = title;
        this.content = content;
        this.dod = dod;
        this.activity = activity;
    }
}
