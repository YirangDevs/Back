package com.api.yirang.notice.generator;

import com.api.yirang.common.domain.region.model.Region;
import com.api.yirang.common.generator.RegionGenerator;
import com.api.yirang.common.generator.StringRandomGenerator;
import com.api.yirang.common.generator.TimeGenerator;
import com.api.yirang.notices.domain.activity.model.Activity;

import java.time.LocalDateTime;
import java.util.Random;

public class ActivityGenerator {

    // range
    private static final int MAX_NOR = 100;
    private static final int MAX_CONTENT_LENGTH = 3000;

    private static Random rand;

    static {
        rand = new Random();
    }

    public static final Activity createRandomActivity(Region region){
        Long nor = Long.valueOf(rand.nextInt(MAX_NOR));
        String content = StringRandomGenerator.generateRandomKoreansWithLength(Long.valueOf(MAX_CONTENT_LENGTH));

        LocalDateTime dtov = TimeGenerator.generateRandomLocalDateTime();
        LocalDateTime dtod = TimeGenerator.generateRandomLocalDateTime();

        return Activity.builder()
                       .nor(nor).content(content)
                       .dtov(dtov).dtod(dtod)
                       .region(region)
                       .build();
    }

    public static final Activity createRandomActivity(){
        Region region = RegionGenerator.generateRandomRegion();
        return createRandomActivity(region);
    }

}
