package com.api.yirang.matching.repository.mongo;

import com.api.yirang.matching.model.mongo.UnMatchingList;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UnMatchingListRepository extends MongoRepository<UnMatchingList, Long> {


    Optional<UnMatchingList> findUnMatchingListByActivityId(Long ActivityId);

    void deleteUnMatchingListByActivityId(Long activityId);
}
