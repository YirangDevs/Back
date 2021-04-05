package com.api.yirang.matching.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
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
}
