package com.api.yirang.common.generator;

import com.api.yirang.apply.support.type.MatchingState;
import com.api.yirang.auth.support.type.Authority;
import com.api.yirang.common.support.type.Region;
import com.api.yirang.common.support.type.Sex;
import com.api.yirang.seniors.support.custom.ServiceType;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EnumGenerator {

    private static final List<Sex> SEXES = Arrays.asList(Sex.values());
    private static final List<ServiceType> SERVICE_TYPES = Arrays.asList(ServiceType.values());
    private static final List<Region> REGIONS = Arrays.asList(Region.values());
    private static final List<Authority> AUTHORITIES = Arrays.asList(Authority.values());

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
    public static final Region generateRandomRegion() {return REGIONS.get(rand.nextInt(REGIONS.size()));}
    public static final Authority generateRandomAuthority(){return AUTHORITIES.get(rand.nextInt(AUTHORITIES.size()));}

}
