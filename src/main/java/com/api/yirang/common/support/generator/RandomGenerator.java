package com.api.yirang.common.support.generator;

import java.util.Random;

public class RandomGenerator {

    private static Random rand;

    static {
        rand = new Random();
    }

    public static String RandomStringOfNumbersWithLength(long length){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++){
            stringBuilder.append(rand.longs(1,0, 10).sum());
        }
        return stringBuilder.toString();
    }
}
