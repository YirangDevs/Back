package com.api.yirang.matching.model.mongo;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.List;

@Document(collection = "un_matching_list")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
@Getter
@ToString
public class UnMatchingList {

    @Id
    private String activityId;

}
