package com.api.yirang.common.distributionRegion;


import com.api.yirang.auth.repository.persistence.maria.AdminDao;
import com.api.yirang.common.repository.persistence.maria.DistributionRegionDao;
import com.api.yirang.common.repository.persistence.maria.RegionDao;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DistributionRegionDaoTest {

    // 테스트 할 Dao
    @Autowired
    DistributionRegionDao distributionRegionDao;

    // 테스트를 위한 Dao
    @Autowired
    AdminDao adminDao;

    @Autowired
    RegionDao regionDao;


}
