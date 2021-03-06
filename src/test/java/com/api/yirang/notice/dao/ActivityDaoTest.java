package com.api.yirang.notice.dao;


import com.api.yirang.common.configure.H2DataBaseConfig;
import com.api.yirang.common.generator.EnumGenerator;
import com.api.yirang.common.support.time.TimeConverter;
import com.api.yirang.common.support.type.Region;
import com.api.yirang.notices.domain.activity.converter.ActivityConverter;
import com.api.yirang.notices.domain.activity.exception.ActivityNullException;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.notices.presentation.dto.embeded.ActivityRegisterRequestDto;
import com.api.yirang.notices.repository.persistence.maria.ActivityDao;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ActivityDaoTest {

    // 실험하고 싶은 Dao
    @Autowired
    ActivityDao activityDao;

    // 보조 Service

    // variables
    Region region = EnumGenerator.generateRandomRegion();
    String dov = "2010-11-07";
    String tov = "23:59:59";

    ActivityRegisterRequestDto activityRegisterRequestDto
            = ActivityRegisterRequestDto.builder()
                                        .content("1234")
                                        .dov(dov).tov("23:59:59")
                                        .dod("2010-11-06")
                                        .nor(Long.valueOf(5)).region(region)
                                        .build();

    @After
    public void tearDown(){
        System.out.println("테스트가 끝났습니다.");
        activityDao.deleteAll();
    }
    // timeAttribute가 제대로 작동하는 지
    @Test
    public void 봉사활동이_잘만들어져서_들어가는지(){
        String dtovStr = dov + " " + tov;
        LocalDateTime dtov = TimeConverter.StringToLocalDateTime(dtovStr);
        Activity activity = ActivityConverter.ConvertFromDtoToModel(activityRegisterRequestDto);
        System.out.println(activity);
        activityDao.save(activity);

        Activity activityFound = activityDao.findActivityByRegionAndDTOV(region, dtov).orElseThrow(ActivityNullException::new);
        System.out.println(activityFound);

        assertThat(activity.getRegion()).isEqualTo(activityFound.getRegion());

    }

}
