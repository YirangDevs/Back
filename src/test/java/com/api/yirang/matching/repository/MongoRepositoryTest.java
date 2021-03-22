package com.api.yirang.matching.repository;


import com.api.yirang.matching.model.maria.Matching;
import com.api.yirang.matching.model.mongo.UnMatchingList;
import com.api.yirang.matching.repository.maria.MatchingRepository;
import com.api.yirang.matching.repository.mongo.UnMatchingListRepository;
import org.junit.After;
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

    @After
    public void tearDown(){
        unMatchingListRepository.deleteAll();
    }

    @Test
    public void 매칭_생셩_후_저장(){

        List<Long> unMatchedSeniorIds = Arrays.asList(11L, 22L, 33L);
        List<Long> unMatchedVolunteerIds = Arrays.asList(44L, 55L, 66L);


        UnMatchingList unMatchingList = UnMatchingList.builder()
                                                      .activityId(1L)
                                                      .seniorIds(unMatchedSeniorIds)
                                                      .volunteerIds(unMatchedVolunteerIds)
                                                      .build();
        unMatchingListRepository.save(unMatchingList);
    }
}
