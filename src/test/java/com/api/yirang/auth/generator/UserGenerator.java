package com.api.yirang.auth.generator;

import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.support.type.Authority;
import com.api.yirang.common.generator.EnumGenerator;
import com.api.yirang.common.generator.NumberRandomGenerator;
import com.api.yirang.common.generator.StringRandomGenerator;
import com.api.yirang.common.support.type.Sex;

import java.util.Random;

/**
 * Created by JeongminYoo on 2021/1/16
 * Work with Yirang
 * Email: likemin0142@gmail.com
 * Blog: http://Beewoom.github.io
 * Github: http://github.com/Biewoom
 */
public class UserGenerator {


    private static Random rand;

    static {
        rand = new Random();
    }

    public static User createRandomUser(Long userId, String username, String realname,
                                        Sex sex, String email, String phone,
                                        Authority authority){

        return User.builder()
                   .userId(userId).username(username)
                   .realname(realname).phone(phone)
                   .sex(sex).email(email).authority(authority)
                   .build();
    }

    public static User createRandomUser(Authority authority){
        Long userId = NumberRandomGenerator.generateLongValueWithRange(1, 100000);
        String username= StringRandomGenerator.generateKoreanNameWithLength(Long.valueOf(3));
        String realname = StringRandomGenerator.generateKoreanNameWithLength(Long.valueOf(3));
        Sex sex = EnumGenerator.generateRandomSex();
        String email = StringRandomGenerator.generateRandomEmailAddress(5L);
        String phone = StringRandomGenerator.generateNumericStringWithLength(Long.valueOf(11));
        return createRandomUser(userId, username, realname, sex, email, phone, authority);
    }
    public static User createRandomUser(Authority authority, Sex sex){
        Long userId = NumberRandomGenerator.generateLongValueWithRange(1, 100000);
        String username= StringRandomGenerator.generateKoreanNameWithLength(Long.valueOf(3));
        String realname = StringRandomGenerator.generateKoreanNameWithLength(Long.valueOf(3));
        String email = StringRandomGenerator.generateRandomEmailAddress(5L);
        String phone = StringRandomGenerator.generateNumericStringWithLength(Long.valueOf(11));
        return createRandomUser(userId, username, realname, sex, email, phone, authority);
    }



    public static User createRandomUser(){
        Long userId = NumberRandomGenerator.generateLongValueWithRange(1, 100000);
        String username= StringRandomGenerator.generateKoreanNameWithLength(Long.valueOf(3));
        String realname = StringRandomGenerator.generateKoreanNameWithLength(Long.valueOf(3));
        Sex sex = EnumGenerator.generateRandomSex();
        String email = StringRandomGenerator.generateRandomEmailAddress(5L);
        String phone = StringRandomGenerator.generateNumericStringWithLength(Long.valueOf(11));
        Authority authority = EnumGenerator.generateRandomAuthority();

        return createRandomUser(userId, username, realname, sex, email, phone, authority);
    }


}
