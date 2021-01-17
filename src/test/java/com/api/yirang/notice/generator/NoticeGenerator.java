package com.api.yirang.notice.generator;

import com.api.yirang.auth.domain.user.model.Admin;
import com.api.yirang.common.generator.StringRandomGenerator;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.notices.domain.notice.model.Notice;

import java.util.Random;

/**
 * Created by JeongminYoo on 2021/1/16
 * Work with Yirang
 * Email: likemin0142@gmail.com
 * Blog: http://Beewoom.github.io
 * Github: http://github.com/Biewoom
 */
public class NoticeGenerator {

    private static Random rand;

    static {
        rand = new Random();
    }

    public static Notice createRandomNotice(Admin admin, Activity activity){
        String title = StringRandomGenerator.generateRandomKoreansWithLength(Long.valueOf(100));

        return Notice.builder()
                     .title(title).admin(admin).activity(activity)
                     .build();
    }

}
