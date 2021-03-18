package com.api.yirang.email.model;


import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.email.util.Consent;
import com.api.yirang.email.util.Validation;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@Getter
@ToString(exclude = {"user"})
@NoArgsConstructor
@Entity
@Table(name = "email")
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emailNumber;

    @Column
    private String certificationNumbers;

    @Enumerated(EnumType.STRING)
    @Column
    private Consent notifiable;

    @Enumerated(EnumType.STRING)
    @Column
    private Validation validation;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Email(User user) {
        this.certificationNumbers = null;
        this.notifiable = Consent.CONSENT_YES;
        this.validation = Validation.VALIDATION_NO;
        this.user = user;
    }
}

