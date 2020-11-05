package com.api.yirang.common.generator;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class TimeGenerator {

    private static final Long MIN_DATETIME = LocalDateTime.of(1970, 1, 1, 0, 0, 0)
                                                          .toEpochSecond(ZoneOffset.ofHours(0));
    private static final Long MAX_DATETIME = LocalDateTime.of(2050, 1, 1, 0, 0, 0)
                                                          .toEpochSecond(ZoneOffset.ofHours(0));


    public static final LocalDateTime generateRandomLocalDateTime(){
        Long randomSecond = MIN_DATETIME + Long.valueOf((long)(Math.random() * (MAX_DATETIME - MIN_DATETIME)));
        final LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(randomSecond, 0, ZoneOffset.ofHours(0));
        return localDateTime;
    }


}
