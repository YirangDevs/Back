package com.api.yirang.notice.dao;


import com.api.yirang.common.domain.region.model.Region;
import com.api.yirang.common.service.RegionService;
import com.api.yirang.notices.domain.activity.converter.ActivityConverter;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.notices.presentation.dto.embeded.ActivityRegisterRequestDto;
import com.api.yirang.notices.repository.persistence.maria.ActivityDao;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityDaoTest {

    // 실험하고 싶은 Dao
    @Autowired
    ActivityDao activityDao;

    // 보조 Service
    @Autowired
    RegionService regionService;

    // variables
    ActivityRegisterRequestDto activityRegisterRequestDto
            = ActivityRegisterRequestDto.builder()
                                        .content("1234")
                                        .dov("2010-11-07").tov("23:59:59")
                                        .dod("2010-11-06")
                                        .nor(Long.valueOf(5)).region("수성구")
                                        .build();

    @After
    public void tearDown(){
        System.out.println("테스트가 끝났습니다.");
        activityDao.deleteAll();
    }
    // timeAttribute가 제대로 작동하는 지
    @Test
    public void 봉사활동이_잘만들어져서_들어가는지(){
        Region region = regionService.findRegionByRegionName("수성구");
        Activity activity = ActivityConverter.ConvertFromDtoToModel(activityRegisterRequestDto, region);
        System.out.println(activity);
        activityDao.save(activity);
    }

}
