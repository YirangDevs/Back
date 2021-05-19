package com.api.yirang.email.dto;

import com.api.yirang.common.support.type.Region;
import com.api.yirang.common.support.type.Sex;
import com.api.yirang.email.util.MailContentConverter;
import com.api.yirang.seniors.support.custom.ServiceType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ActivityGuideMailContent {

    private final String volunteerName;
    private final String yearOfActivity;
    private final String monthOfActivity;
    private final String dayOfActivity;
    private final String hourOfActivity;
    private final String minutesOfActivity;

    private final String paOfStartTime;
    private final String paOfEndTime;
    private final String startHourOfActivity;
    private final String endHourOfActivity;
    private final String dayOfWeekOfActivity;

    private final String roadAddress;
    private final String areaAddress;

    private final String seniorName;
    private final String seniorPhone;
    private final String seniorSex;

    private final String serviceType;

    private final String staticMapUrl;

    @Builder
    public ActivityGuideMailContent(String volunteerName,
                                    LocalDateTime activityTime, Region region,
                                    String roadAddress, String areaAddress,
                                    String seniorName, String seniorPhone, Sex seniorSex,
                                    ServiceType serviceType, String staticMapUrl) {

        Map<String, String> timeMap =  MailContentConverter.localDateTimeToMailContent(activityTime);

        this.volunteerName = volunteerName;
        this.yearOfActivity = timeMap.get("year");
        this.monthOfActivity = timeMap.get("month");
        this.dayOfActivity = timeMap.get("day");
        this.hourOfActivity = timeMap.get("hour");
        this.minutesOfActivity = timeMap.get("minute");

        this.startHourOfActivity = String.valueOf(activityTime.getHour() % 12);
        this.endHourOfActivity = String.valueOf( (activityTime.getHour() + 2) % 12 );
        this.dayOfWeekOfActivity = MailContentConverter.dayOfWeeksToKoreanContentString(activityTime.getDayOfWeek().getValue());
        this.paOfStartTime = (activityTime.getHour() ) >= 12 ? "오후" : "오전";
        this.paOfEndTime = (activityTime.getHour() + 2 ) >= 12 ? "오후" : "오전";


        this.roadAddress = "대구시 " + region.getRegionName() + " " + roadAddress;
        this.areaAddress = areaAddress;
        this.seniorName = seniorName;
        this.seniorPhone = seniorPhone;
        this.seniorSex = MailContentConverter.sexToMailContentString(seniorSex);
        this.serviceType = MailContentConverter.serviceTypeToEmailContentString(serviceType);
        this.staticMapUrl = staticMapUrl;
    }
}
