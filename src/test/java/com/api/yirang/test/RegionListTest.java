package com.api.yirang.test;

import com.api.yirang.common.exceptions.NullException;
import com.api.yirang.common.generator.EnumGenerator;
import com.api.yirang.common.support.converter.RegionListConverter;
import com.api.yirang.common.support.type.Region;
import com.api.yirang.test.dao.RegionListTestDao;
import com.api.yirang.test.model.RegionListTestModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class RegionListTest {

    @Autowired
    private RegionListTestDao regionListTestDao;

    // variable
    Region region1 = EnumGenerator.generateRandomRegion();
    Region region2 = EnumGenerator.generateRandomRegion();
    Region region3 = EnumGenerator.generateRandomRegion();

    Long id1 = Long.valueOf(1);
    Long id2 = Long.valueOf(2);
    Long id3 = Long.valueOf(3);

    @After
    public void tearDown(){
        regionListTestDao.deleteAll();
    }

    @Test
    public void 지역_안넣고_확인(){
        RegionListTestModel regionListTestModel = RegionListTestModel.builder()
                                                                     .id(Long.valueOf(id1))
                                                                     .build();
        regionListTestDao.save(regionListTestModel);
        RegionListTestModel output = regionListTestDao.findById(id1).orElseThrow(() -> new NullException("Good"));
        System.out.println("regionListModel: " + output);

        assertThat(output.getId()).isEqualTo(id1);
        assertThat(output.getRegionList()).isEmpty();
    }
    @Test
    public void 지역_한번_넣고_확인(){
        지역_안넣고_확인();
        RegionListTestModel output = regionListTestDao.findById(id1).orElseThrow(() -> new NullException("GOOD"));
        List<Region> regions = output.getRegionList();
        regions.add(region1);
        regionListTestDao.updateRegionsByUserId(id1, RegionListConverter.convertFromListToString(regions));

        // 꺼내서 확인
        RegionListTestModel output2 = regionListTestDao.findById(id1).orElseThrow(() -> new NullException("Good"));
        System.out.println("한 번 넣고 regionListModel: " + output);
        System.out.println(output2);
        assertThat(output.getId()).isEqualTo(id1);
        assertThat(output.getRegionList()).contains(region1);
    }

    @Test
    public void 지역_두_번_넣어서_확인(){
        지역_한번_넣고_확인();
        RegionListTestModel output = regionListTestDao.findById(id1).orElseThrow(() -> new NullException("GOOD"));
        List<Region> regions = output.getRegionList();
        regions.add(region2);
        regionListTestDao.updateRegionsByUserId(id1, RegionListConverter.convertFromListToString(regions));

        // 꺼내서 확인
        RegionListTestModel output2 = regionListTestDao.findById(id1).orElseThrow(() -> new NullException("Good"));
        System.out.println(output2);
        assertThat(output.getId()).isEqualTo(id1);
        assertThat(output.getRegionList()).contains(region1);
        assertThat(output.getRegionList()).contains(region2);
    }
    @Test
    public void 지역_있는걸_지워보기(){

    }

}
