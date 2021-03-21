package com.api.yirang.matching.repository.mongo;

import com.api.yirang.matching.model.mongo.UnMatchingList;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UnMatchingListRepository extends MongoRepository<UnMatchingList, String> {

    UnMatchingList findUnMatchingListByActivityId(String ActivityId);

}
