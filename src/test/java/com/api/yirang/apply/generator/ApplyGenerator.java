package com.api.yirang.apply.generator;

import com.api.yirang.apply.domain.model.Apply;
import com.api.yirang.auth.domain.user.model.Volunteer;
import com.api.yirang.auth.generator.VolunteerGenerator;
import com.api.yirang.common.generator.EnumGenerator;
import com.api.yirang.notice.generator.ActivityGenerator;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.seniors.support.custom.ServiceType;


public class ApplyGenerator {


    public static Apply createRandomApply(Volunteer volunteer, Activity activity){

        ServiceType serviceType = EnumGenerator.generateRandomServiceType();

        return Apply.builder()
                    .activity(activity)
                    .volunteer(volunteer)
                    .serviceType(serviceType)
                    .build();
    }
    public static Apply createRandomApply(Activity activity){

        Volunteer volunteer = VolunteerGenerator.createRandomVolunteer();

        return createRandomApply(volunteer, activity);

    }
    public static Apply createRandomApply(Volunteer volunteer){

        Activity activity = ActivityGenerator.createRandomActivity();

        return createRandomApply(volunteer, activity);

    }

    public static Apply createRandomApply(){

        Volunteer volunteer = VolunteerGenerator.createRandomVolunteer();
        Activity activity = ActivityGenerator.createRandomActivity();

        return createRandomApply(volunteer, activity);
    }

}
