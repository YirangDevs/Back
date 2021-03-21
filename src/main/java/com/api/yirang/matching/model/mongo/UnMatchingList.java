package com.api.yirang.matching.model.mongo;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.List;

@Document(collection = "un_matching_list")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
public class UnMatchingList {

    @Id
    private Long activityId;

    private List<Long> seniorIds;

    private List<Long> volunteerIds;

    @Builder
    public UnMatchingList(Long activityId, List<Long> seniorIds, List<Long> volunteerIds) {
        this.activityId = activityId;
        this.seniorIds = seniorIds;
        this.volunteerIds = volunteerIds;
    }
}
