package com.api.yirang.email.util;


import com.api.yirang.auth.support.type.Authority;
import com.api.yirang.common.support.type.Sex;

public class MailContentConverter {

    public static String authorityToMailContentString(Authority authority){
        return authority.equals(Authority.ROLE_ADMIN) ? "관리자" :
               authority.equals(Authority.ROLE_SUPER_ADMIN) ? "슈퍼_관리자" : "봉사자";
    }

    public static String sexToMailContentString(Sex sex){
        return sex.equals(Sex.SEX_MALE) ? "남성" :
               sex.equals(Sex.SEX_FEMALE) ? "여성" : "성별모름";
    }
}
