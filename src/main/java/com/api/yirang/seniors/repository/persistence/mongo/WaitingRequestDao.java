package com.api.yirang.seniors.repository.persistence.mongo;

import com.api.yirang.seniors.domain.waitingRequest.model.WaitingRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import static com.api.yirang.seniors.domain.waitingRequest.model.WaitingRequest.CompositeKey;

public interface WaitingRequestDao extends MongoRepository<WaitingRequest,CompositeKey> {
    WaitingRequest findWaitingRequestById(CompositeKey compositekey);

}
