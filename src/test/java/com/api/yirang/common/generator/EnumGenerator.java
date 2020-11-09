package com.api.yirang.common.generator;

import com.api.yirang.common.support.type.Sex;
import com.api.yirang.seniors.support.custom.ServiceType;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EnumGenerator {

    private static final List<Sex> SEXES = Arrays.asList(Sex.values());
    private static final List<ServiceType> SERVICE_TYPES = Arrays.asList(ServiceType.values());
    private static Random rand;

    static{
        rand = new Random();
    }


    public static final Sex generateRandomSex(){
        return SEXES.get(rand.nextInt(SEXES.size()));
    }
    public static final ServiceType generateRandomServiceType(){
        return SERVICE_TYPES.get(rand.nextInt(SERVICE_TYPES.size()));
    }
}
