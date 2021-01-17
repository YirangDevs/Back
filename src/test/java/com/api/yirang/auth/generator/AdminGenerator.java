package com.api.yirang.auth.generator;

import com.api.yirang.auth.domain.user.model.Admin;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.common.support.type.Region;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by JeongminYoo on 2021/1/16
 * Work with Yirang
 * Email: likemin0142@gmail.com
 * Blog: http://Beewoom.github.io
 * Github: http://github.com/Biewoom
 */
public class AdminGenerator {

    private static Random rand;

    static{
        rand = new Random();
    }

    private static Admin createRandomAdmin(User user, List<Region> regions){
        return Admin.builder()
                    .user(user).regions(regions)
                    .build();
    }

    private static Admin createRandomAdmin(User user){
        return createRandomAdmin(user, new ArrayList<>());
    }
}

