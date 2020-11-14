package com.api.yirang.seniors.domain.waitingRequest.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.io.Serializable;


@Document(collection = "waiting_request")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
public class WaitingRequest {

    @Id
    private CompositeKey id;

    private String name;

    @Value
    public static class CompositeKey{
        private String studentId;
        private String groupId;
    }

    @Builder
    public WaitingRequest(String studentId, String groupId, String name) {
        this.id = new CompositeKey(studentId, groupId);
        this.name = name;
    }
}
