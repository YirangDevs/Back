package com.api.yirang.common.generator;

import java.util.Random;

/**
 * Created by JeongminYoo on 2020/11/28
 * Work with Yirang
 * Email: likemin0142@gmail.com
 * Blog: http://Beewoom.github.io
 * Github: http://github.com/Biewoom
 */
public class NumberRandomGenerator {

    private static Random rand;

    static{
        rand = new Random();
    }

    public static Long generateLongValueWithRange(int start, int end){
        return rand.longs(1, start, end-start).sum();
    }
}
