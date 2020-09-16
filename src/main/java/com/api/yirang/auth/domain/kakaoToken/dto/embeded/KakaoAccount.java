package com.api.yirang.auth.domain.kakaoToken.dto.embeded;


import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
public class KakaoAccount {
    @SerializedName("has_email")
    private final boolean hasEmail;
    @SerializedName("email_needs_agreement")
    private final boolean emailNeedsAgreement;
    @SerializedName("is_email_valid")
    private final boolean isEmailValid;
    @SerializedName("is_email_verified")
    private final boolean isEmailVerified;

    private final String email;

    @SerializedName("has_gender")
    private final boolean hasGender;

    @SerializedName("gender_needs_agreement")
    private final boolean genderNeedsAgreement;

    private final String gender;

    public KakaoAccount() {
        this.hasEmail = false;
        this.emailNeedsAgreement = false;
        this.isEmailValid = false;
        this.isEmailVerified = false;
        this.email = null;
        this.hasGender = false;
        this.genderNeedsAgreement = false;
        this.gender = null;
    }
}
