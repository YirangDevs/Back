package com.api.yirang.common.support.time;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeConverter {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static LocalDateTime StringToLocalDateTime(String timeStr){
        return LocalDateTime.parse(timeStr, FORMATTER);
    }

    public static String LocalDateTimeToString(LocalDateTime localDateTime){
        return localDateTime.format(FORMATTER);
    }
}
