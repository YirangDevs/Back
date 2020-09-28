package com.api.yirang.common.support.time;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MyLocalTime {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String makeExpiredTimeString(Long plusTime){
        LocalDateTime now = LocalDateTime.now();
        return now.plusSeconds(plusTime).format(FORMATTER);
    }

    public static String makeCurrentTimeString(){
        LocalDateTime now = LocalDateTime.now();
        return now.format(FORMATTER);
    }
}
