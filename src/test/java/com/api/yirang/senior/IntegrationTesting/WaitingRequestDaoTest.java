package com.api.yirang.senior.IntegrationTesting;

import com.api.yirang.common.generator.StringRandomGenerator;
import com.api.yirang.seniors.domain.waitingRequest.model.WaitingRequest;
import com.api.yirang.seniors.repository.persistence.mongo.WaitingRequestDao;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.api.yirang.seniors.domain.waitingRequest.model.WaitingRequest.CompositeKey;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WaitingRequestDaoTest {

    @Autowired
    private WaitingRequestDao waitingRequestDao;

    @After
    public void tearDown(){
        waitingRequestDao.deleteAll();
    }

    @Test
    public void 저장하고_뺴기(){
        Long idLength = Long.valueOf(5);
        Long nameLength = Long.valueOf(3);

        String studentId = StringRandomGenerator.generateNumericStringWithLength(idLength);
        String groupId = StringRandomGenerator.generateNumericStringWithLength(idLength);

        WaitingRequest waitingRequest = WaitingRequest.builder()
                                                      .studentId(studentId)
                                                      .groupId(groupId)
                                                      .name(StringRandomGenerator.generateRandomKoreansWithLength(nameLength))
                                                      .build();
        System.out.println(waitingRequest);

        // 저장하기
        waitingRequestDao.save(waitingRequest);

        // 다시 뺴기
        CompositeKey compositeKey = new CompositeKey(studentId, groupId);
        WaitingRequest testWaitingRequest = waitingRequestDao.findWaitingRequestById(compositeKey);

        assertThat(testWaitingRequest.getName()).isEqualTo(waitingRequest.getName());
        }
}
