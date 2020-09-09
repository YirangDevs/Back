package com.api.yirang.auth.domain.kakaoToken.dto.embeded;


import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class KakaoAccount {
    @SerializedName("has_email")
    private boolean hasEmail;
    @SerializedName("email_needs_agreement")
    private boolean emailNeedsAgreement;
    @SerializedName("is_email_valid")
    private boolean isEmailValid;
    @SerializedName("is_email_verified")
    private boolean isEmailVerified;
    //!!
    private String email;

    @SerializedName("has_gender")
    private boolean hasGender;

    @SerializedName("gender_needs_agreement")
    private boolean genderNeedsAgreement;

    private String gender;


}
