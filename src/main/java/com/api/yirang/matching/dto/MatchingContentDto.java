package com.api.yirang.matching.dto;

import com.api.yirang.common.support.type.Sex;
import com.api.yirang.seniors.support.custom.ServiceType;
import lombok.Builder;
import lombok.Data;

@Data
public class MatchingContentDto {

    // senior 정보
    private final String seniorName;
    private final Long seniorId;
    private final Sex seniorSex;
    private final ServiceType serviceType;
    private final String seniorPhone;

    private final String volunteerName;
    private final Long volunteerId;
    private final String volunteerProfileImg;
    private final String volunteerEmail;
    private final Sex volunteerSex;
    private final String volunteerPhone;

    public MatchingContentDto() {
        this.seniorName = null;
        this.seniorId = null;
        this.volunteerName = null;
        this.volunteerId = null;
        this.seniorSex = null;
        this.serviceType = null;
        this.seniorPhone = null;

        this.volunteerProfileImg = null;
        this.volunteerEmail = null;
        this.volunteerSex = null;
        this.volunteerPhone = null;
    }

    @Builder
    public MatchingContentDto(String seniorName, Long seniorId, Sex seniorSex, ServiceType serviceType, String seniorPhone, String volunteerName,
                              Long volunteerId, String volunteerProfileImg, String volunteerEmail, Sex volunteerSex, String volunteerPhone) {
        this.seniorName = seniorName;
        this.seniorId = seniorId;
        this.seniorSex = seniorSex;
        this.serviceType = serviceType;
        this.seniorPhone = seniorPhone;
        this.volunteerName = volunteerName;
        this.volunteerId = volunteerId;
        this.volunteerProfileImg = volunteerProfileImg;
        this.volunteerEmail = volunteerEmail;
        this.volunteerSex = volunteerSex;
        this.volunteerPhone = volunteerPhone;
    }
}
