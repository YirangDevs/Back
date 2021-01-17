package com.api.yirang.auth.generator;

import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.domain.user.model.Volunteer;

import java.util.Random;

/**
 * Created by JeongminYoo on 2021/1/17
 * Work with Yirang
 * Email: likemin0142@gmail.com
 * Blog: http://Beewoom.github.io
 * Github: http://github.com/Biewoom
 */
public class VolunteerGenerator {

    private static Random rand;

    static {
        rand = new Random();
    }

    public static Volunteer createRandomVolunteer(User user){
        return Volunteer.builder()
                        .user(user)
                        .build();
    }

}
