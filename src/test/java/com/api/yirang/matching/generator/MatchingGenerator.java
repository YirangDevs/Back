package com.api.yirang.matching.generator;

import com.api.yirang.auth.domain.user.model.Volunteer;
import com.api.yirang.auth.generator.VolunteerGenerator;
import com.api.yirang.common.generator.EnumGenerator;
import com.api.yirang.common.generator.TimeGenerator;
import com.api.yirang.matching.model.maria.Matching;
import com.api.yirang.matching.repository.maria.MatchingRepository;
import com.api.yirang.notice.generator.ActivityGenerator;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.senior.generator.SeniorGenerator;
import com.api.yirang.seniors.domain.senior.model.Senior;
import com.api.yirang.seniors.support.custom.ServiceType;

import java.util.Random;

public class MatchingGenerator {


    public static Matching createRandomMatching(Senior senior, Volunteer volunteer,
                                                Activity activity){

        ServiceType serviceType = EnumGenerator.generateRandomServiceType();

        return Matching.builder()
                       .senior(senior)
                       .serviceType(serviceType)
                       .volunteer(volunteer)
                       .activity(activity)
                       .build();
    }

    public static Matching createRandomMatching(Activity activity){

        Senior senior = SeniorGenerator.createRandomSenior();
        Volunteer volunteer = VolunteerGenerator.createRandomVolunteer();

        return createRandomMatching(senior, volunteer, activity);
    }

    public static Matching createRandomMatching(Volunteer volunteer){

        Senior senior = SeniorGenerator.createRandomSenior();
        Activity activity = ActivityGenerator.createRandomActivity();

        return createRandomMatching(senior, volunteer, activity);

    }

    public static Matching createAndStoreRandomMatching(MatchingRepository matchingRepository, Activity activity, Senior senior, Volunteer volunteer){
        return matchingRepository.save(createRandomMatching(senior, volunteer, activity));
    }
}
