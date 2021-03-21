package com.api.yirang.matching.repository;


import com.api.yirang.matching.model.maria.Matching;
import com.api.yirang.matching.model.mongo.UnMatchingList;
import com.api.yirang.matching.repository.maria.MatchingRepository;
import com.api.yirang.matching.repository.mongo.UnMatchingListRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoRepositoryTest {

    @Autowired
    UnMatchingListRepository unMatchingListRepository;

    @Test
    public void 매칭_생셩_후_저장(){

        List<String> unMatchedSeniorIds = Arrays.asList("11", "22", "33");
        List<String> unMatchedVolunteerIds = Arrays.asList("44", "55", "66");

        UnMatchingList unMatchingList = UnMatchingList.builder()
                                                      .activityId("111")
                                                      .build();
        unMatchingListRepository.save(unMatchingList);
    }
}
