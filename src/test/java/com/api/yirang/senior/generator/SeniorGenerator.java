package com.api.yirang.senior.generator;


import com.api.yirang.common.generator.EnumGenerator;
import com.api.yirang.common.generator.StringRandomGenerator;
import com.api.yirang.common.support.type.Region;
import com.api.yirang.common.support.type.Sex;
import com.api.yirang.seniors.domain.senior.model.Senior;


public class SeniorGenerator {

    public static final Senior createRandomSenior(){
        String seniorName = StringRandomGenerator.generateKoreanNameWithLength(Long.valueOf(2));
        Sex sex = EnumGenerator.generateRandomSex();
        String address = StringRandomGenerator.generateRandomKoreansWithLength(Long.valueOf(10));
        String phone = StringRandomGenerator.generateNumericStringWithLength(Long.valueOf(11));
        Region region = EnumGenerator.generateRandomRegion();

        return Senior.builder()
                     .seniorName(seniorName).sex(sex)
                     .address(address).phone(phone).region(region)
                     .build();

    }
    public static final Senior createRandomSenior(String seniorName){
        Sex sex = EnumGenerator.generateRandomSex();
        String address = StringRandomGenerator.generateRandomKoreansWithLength(Long.valueOf(10));
        String phone = StringRandomGenerator.generateNumericStringWithLength(Long.valueOf(11));
        Region region = EnumGenerator.generateRandomRegion();

        return Senior.builder()
                     .seniorName(seniorName).sex(sex)
                     .address(address).phone(phone).region(region)
                     .build();

    }

    public static final Senior createRandomSenior(Region region){
        String seniorName = StringRandomGenerator.generateKoreanNameWithLength(Long.valueOf(2));
        Sex sex = EnumGenerator.generateRandomSex();
        String address = StringRandomGenerator.generateRandomKoreansWithLength(Long.valueOf(10));
        String phone = StringRandomGenerator.generateNumericStringWithLength(Long.valueOf(11));

        return Senior.builder()
                     .seniorName(seniorName).sex(sex)
                     .address(address).phone(phone).region(region)
                     .build();

    }

    public static final Senior createRandomSeniorWithPhone(String phone){
        String seniorName = StringRandomGenerator.generateKoreanNameWithLength(Long.valueOf(2));
        Sex sex = EnumGenerator.generateRandomSex();
        String address = StringRandomGenerator.generateRandomKoreansWithLength(Long.valueOf(10));
        Region region = EnumGenerator.generateRandomRegion();

        return Senior.builder()
                     .seniorName(seniorName).sex(sex)
                     .address(address).phone(phone).region(region)
                     .build();

    }
}
