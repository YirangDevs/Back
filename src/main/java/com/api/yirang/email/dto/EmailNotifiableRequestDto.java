package com.api.yirang.email.dto;

import com.api.yirang.email.util.Consent;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class EmailNotifiableRequestDto {


    @NotNull
    private final Consent notifiable;

    public EmailNotifiableRequestDto() {
        this.notifiable = null;
    }
}
