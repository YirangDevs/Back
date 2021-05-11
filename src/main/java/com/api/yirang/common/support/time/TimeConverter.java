package com.api.yirang.common.support.time;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Stream;

public class TimeConverter {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static LocalDateTime StringToLocalDateTime(String timeStr){
        return LocalDateTime.parse(timeStr, FORMATTER);
    }

    public static String LocalDateTimeToString(LocalDateTime localDateTime){
        return localDateTime.format(FORMATTER);
    }

    public static String LocalDateTimeToMailContentString(LocalDateTime localDateTime){
        String timestamp = localDateTime.format(FORMATTER);
        String[] ymd_list = timestamp.split(" ")[0].split("-");

        return String.format("%s년 %s월 %s일",ymd_list[0], ymd_list[1], ymd_list[2]);
    }
    public static String LocalDateTimeToMailContentStringV2(LocalDateTime localDateTime){
        String timestamp = localDateTime.format(FORMATTER);
        return timestamp.split(" ")[0];
    }
    public static String LocalDateTimeToMailContentStringV3(LocalDateTime localDateTime){
        String timestamp = localDateTime.format(FORMATTER);
        return timestamp.split(" ")[1];
    }

    public static String[] LocalDateTimeToMailContentStringVersion2(LocalDateTime localDateTime) {
        String timestamp = localDateTime.format(FORMATTER);

        return Stream.concat(Arrays.stream(timestamp.split(" ")[0].split("-") ),
                             Arrays.stream(timestamp.split(" ")[1].split(":") ) ).toArray(String[]::new);

    }
}
