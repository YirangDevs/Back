package com.api.yirang.matching.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class MatchingContentDto {

    private final String seniorName;
    private final Long seniorId;
    private final String volunteerName;
    private final Long volunteerId;

    public MatchingContentDto() {
        this.seniorName = null;
        this.seniorId = null;
        this.volunteerName = null;
        this.volunteerId = null;
    }

    @Builder
    public MatchingContentDto(String seniorName, Long seniorId, String volunteerName, Long volunteerId) {
        this.seniorName = seniorName;
        this.seniorId = seniorId;
        this.volunteerName = volunteerName;
        this.volunteerId = volunteerId;
    }
}
