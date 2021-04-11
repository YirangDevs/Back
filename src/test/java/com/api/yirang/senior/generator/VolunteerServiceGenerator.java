package com.api.yirang.senior.generator;

import com.api.yirang.common.generator.EnumGenerator;
import com.api.yirang.notice.generator.ActivityGenerator;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.seniors.domain.senior.model.Senior;
import com.api.yirang.seniors.domain.volunteerService.model.VolunteerService;
import com.api.yirang.seniors.repository.persistence.maria.VolunteerServiceDao;
import com.api.yirang.seniors.support.custom.ServiceType;

import java.util.Random;

public class VolunteerServiceGenerator {

    private static final int PRIORITY_RIGHT_LIMIT = 5;
    private static Random rand;

    static {
        rand = new Random();
    }

    public static VolunteerService createRandomVolunteerService(Senior senior, Activity activity){
        ServiceType serviceType = EnumGenerator.generateRandomServiceType();
        Long priority = Long.valueOf(rand.nextInt(PRIORITY_RIGHT_LIMIT));

        return VolunteerService.builder()
                               .serviceType(serviceType).priority(priority)
                               .senior(senior).activity(activity)
                               .numsOfRequiredVolunteers(2L)
                               .build();
    }
    public static VolunteerService createRandomVolunteerService(Senior senior, Activity activity, ServiceType serviceType){
        Long priority = Long.valueOf(rand.nextInt(PRIORITY_RIGHT_LIMIT));

        return VolunteerService.builder()
                               .serviceType(serviceType).priority(priority)
                               .senior(senior).activity(activity)
                               .numsOfRequiredVolunteers(2L)
                               .build();
    }

    public static VolunteerService createRandomVolunteerService(Activity activity){
        Senior senior = SeniorGenerator.createRandomSenior();
        return createRandomVolunteerService(senior, activity);
    }
    public static VolunteerService createRandomVolunteerService(Senior senior){
        Activity activity = ActivityGenerator.createRandomActivity();
        return createRandomVolunteerService(senior, activity);
    }

    public static VolunteerService createRandomVolunteerService(){
        Senior senior = SeniorGenerator.createRandomSenior();
        Activity activity = ActivityGenerator.createRandomActivity();
        return createRandomVolunteerService(senior, activity);
    }

    public static VolunteerService createAndStoreRandomVolunteerService(VolunteerServiceDao volunteerServiceDao,
                                                                        Senior senior,
                                                                        Activity activity,
                                                                        ServiceType serviceType){
        return volunteerServiceDao.save(createRandomVolunteerService(senior, activity, serviceType) );
    }



}
