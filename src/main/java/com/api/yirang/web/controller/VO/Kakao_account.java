package com.api.yirang.web.controller.VO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Kakao_account {
    private Object profile;
    private String email;
    private boolean email_needs_agreement;
    private boolean is_email_valid;
    private boolean profile_needs_agreement;
    private boolean has_email;
    private boolean is_email_verified;
}
