package com.api.yirang.auth.generator;

import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.domain.user.model.Volunteer;
import com.api.yirang.auth.repository.persistence.maria.VolunteerDao;
import com.api.yirang.auth.support.type.Authority;
import com.api.yirang.common.support.type.Sex;

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

    public static Volunteer createRandomVolunteer(){
        User user = UserGenerator.createRandomUser(Authority.ROLE_VOLUNTEER);
        return createRandomVolunteer(user);
    }
    public static Volunteer createRandomVolunteer(Sex sex){
        User user = UserGenerator.createRandomUser(Authority.ROLE_VOLUNTEER, sex);
        return createRandomVolunteer(user);
    }
    public static Volunteer createRandomVolunteer(Sex sex, String email){
        User user = UserGenerator.createRandomUser(Authority.ROLE_VOLUNTEER, sex, email);
        return createRandomVolunteer(user);
    }


    public static Volunteer createAndRandomVolunteer(VolunteerDao volunteerDao){
        return volunteerDao.save(createRandomVolunteer());
    }

    public static Volunteer createAndRandomVolunteer(VolunteerDao volunteerDao, Sex sex){
        return volunteerDao.save(createRandomVolunteer(sex));
    }

    public static Volunteer createAndRandomVolunteer(VolunteerDao volunteerDao, Sex sex, String email){
        return volunteerDao.save(createRandomVolunteer(sex, email));
    }




}
