package com.api.yirang.common.region;


import com.api.yirang.common.domain.region.exception.AlreadyExistedRegion;
import com.api.yirang.common.domain.region.exception.RegionNullException;
import com.api.yirang.common.domain.region.model.Region;
import com.api.yirang.common.presentation.dto.RegionRequestDto;
import com.api.yirang.common.service.RegionService;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RegionServiceTest {


    // Test할 Service
    @Autowired
    RegionService regionService;

    // variable
    String regionName = "실험구";

    @After
    public void tearDown() {
        System.out.println("테스트가 완료되고 지역이름을 삭제합니다.");
        if (regionService.isExistedByRegionName(regionName)) {
            regionService.deleteByRegionName(regionName);
        }
    }

    @Test
    public void 지역_이름_저장하기(){

        regionService.save(RegionRequestDto.builder()
                                           .regionName(regionName)
                                           .build());
        //  저장 한거랑 지역 이름이 같은가?
        Region region = regionService.findRegionByRegionName(regionName);
        assertThat(region.getRegionName()).isEqualTo(regionName);
    }

    @Test(expected = AlreadyExistedRegion.class)
    public void 중복한_지역_이름_저장하기(){

        RegionRequestDto dto1 = RegionRequestDto.builder()
                                                .regionName(regionName)
                                                .build();
        RegionRequestDto dto2 = RegionRequestDto.builder()
                                                .regionName(regionName)
                                                .build();
        // 중복해서 실행하게 된다면 에러가 나와야함
        regionService.save(dto1);
        regionService.save(dto2);
    }

    @Test
    public void 지역_이름_삭제하기(){

        regionService.save(RegionRequestDto.builder()
                                           .regionName(regionName)
                                           .build());
        regionService.deleteByRegionName(regionName);

        // 저장하고 다시 삭제하면 없어야 함
        assertFalse(regionService.isExistedByRegionName(regionName));
    }

    @Test(expected = RegionNullException.class)
    public void 없는_지역_삭제하기(){
        regionService.deleteByRegionName(regionName);
    }

}
