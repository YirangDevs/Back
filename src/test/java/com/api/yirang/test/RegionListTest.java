package com.api.yirang.test;

import com.api.yirang.common.generator.EnumGenerator;
import com.api.yirang.common.support.type.Region;
import com.api.yirang.test.dao.RegionListTestDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

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

    @Test
    public void 지역_한개_넣고_확인(){
        RegionListTest regionListTest = RegionListTest
    }

}
