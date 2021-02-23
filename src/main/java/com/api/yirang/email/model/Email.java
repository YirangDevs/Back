package com.api.yirang.email.model;


import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.email.util.Consent;
import com.api.yirang.email.util.Validation;
import com.sun.istack.Nullable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

    @Nullable
    @Column
    private String emailAddress;

    @Nullable
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
    public Email(String emailAddress, String certificationNumbers, User user) {
        this.emailAddress = emailAddress;
        this.certificationNumbers = null;
        this.notifiable = Consent.CONSENT_YES;
        this.validation = Validation.VALIDATION_NO;
        this.user = user;
    }
}

