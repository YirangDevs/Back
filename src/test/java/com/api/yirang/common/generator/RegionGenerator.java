package com.api.yirang.common.generator;

import com.api.yirang.common.domain.region.model.Region;

import java.util.Random;

public class RegionGenerator {

    private static Random rand;

    static{
        rand = new Random();
    }

    public static final Region generateRandomRegion(){
        String regionName = StringRandomGenerator.generateRandomKoreanGu();
        return Region.builder()
                     .regionName(regionName)
                     .build();
    }
}
