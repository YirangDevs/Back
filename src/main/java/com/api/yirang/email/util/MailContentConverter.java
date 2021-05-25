package com.api.yirang.email.util;


import com.api.yirang.auth.support.type.Authority;
import com.api.yirang.common.support.time.TimeConverter;
import com.api.yirang.common.support.type.Sex;
import com.api.yirang.seniors.support.custom.ServiceType;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class MailContentConverter {

    public static String authorityToMailContentString(Authority authority){
        return authority.equals(Authority.ROLE_ADMIN) ? "관리자" :
               authority.equals(Authority.ROLE_SUPER_ADMIN) ? "슈퍼_관리자" : "봉사자";
    }

    public static String sexToMailContentString(Sex sex){
        return sex.equals(Sex.SEX_MALE) ? "남성" :
               sex.equals(Sex.SEX_FEMALE) ? "여성" : "성별모름";
    }


    public static String serviceTypeToEmailContentString(ServiceType serviceType) {
        return serviceType.equals(ServiceType.SERVICE_TALK) ? "말벗봉사" :
               serviceType.equals(ServiceType.SERVICE_WORK) ? "노력봉사" : "상관없음";
    }

    public static String dayOfWeeksToKoreanContentString(Integer intValueOfDayOfWeeks){
        return  intValueOfDayOfWeeks.equals(1) ? "월" :
                intValueOfDayOfWeeks.equals(2) ? "화" :
                intValueOfDayOfWeeks.equals(3) ? "수" :
                intValueOfDayOfWeeks.equals(4) ? "목" :
                intValueOfDayOfWeeks.equals(5) ? "금" :
                intValueOfDayOfWeeks.equals(6) ? "토" :
                intValueOfDayOfWeeks.equals(7) ? "일" : null;
    }


    public static Map<String, String> localDateTimeToMailContent(LocalDateTime localDateTime){
        String[] times = TimeConverter.LocalDateTimeToMailContentStringVersion2(localDateTime);

        Map<String, String> res = new HashMap<>();
        res.put("year", times[0]);
        res.put("month", times[1]);
        res.put("day", times[2]);
        res.put("hour", times[3]);
        res.put("minute", times[4]);

        return res;
    }

}
