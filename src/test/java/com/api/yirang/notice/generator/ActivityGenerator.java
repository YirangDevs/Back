package com.api.yirang.notice.generator;

import com.api.yirang.common.generator.EnumGenerator;
import com.api.yirang.common.generator.StringRandomGenerator;
import com.api.yirang.common.generator.TimeGenerator;
import com.api.yirang.common.support.type.Region;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.notices.repository.persistence.maria.ActivityDao;

import java.time.LocalDateTime;
import java.util.Random;

public class ActivityGenerator {

    // range
    private static final int MAX_NOR = 100;
    private static final int MAX_CONTENT_LENGTH = 100;

    private static Random rand;

    static {
        rand = new Random();
    }

    public static Activity createRandomActivity(Region region, LocalDateTime dtov){
        Long nor = Long.valueOf(rand.nextInt(MAX_NOR));
        String content = StringRandomGenerator.generateRandomKoreansWithLength(Long.valueOf(MAX_CONTENT_LENGTH));

        LocalDateTime dtod = TimeGenerator.generateRandomLocalDateTime();

        return Activity.builder()
                       .nor(nor).content(content)
                       .dtov(dtov).dtod(dtod)
                       .region(region)
                       .build();

    }

    public static Activity createRandomActivity(Region region){
        LocalDateTime dtov = TimeGenerator.generateRandomLocalDateTime();
        return createRandomActivity(region, dtov);
    }
    public static Activity createRandomActivity(LocalDateTime dtov){
        Region region = EnumGenerator.generateRandomRegion();
        return createRandomActivity(region, dtov);
    }

    public static Activity createRandomActivity(){
        Region region = EnumGenerator.generateRandomRegion();
        LocalDateTime dtov = TimeGenerator.generateRandomLocalDateTime();
        return createRandomActivity(region, dtov);
    }

    public static Activity createAndStoreRandomActivity(ActivityDao activityDao){
        return activityDao.save(createRandomActivity());
    }

}
