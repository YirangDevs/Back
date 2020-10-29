package com.api.yirang.common.distributionRegion;


import com.api.yirang.auth.application.basicService.AdminService;
import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.auth.domain.user.exceptions.AdminNullException;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.common.domain.region.exception.DistributionRegionNullException;
import com.api.yirang.common.domain.region.exception.RegionNullException;
import com.api.yirang.common.domain.region.model.DistributionRegion;
import com.api.yirang.common.presentation.dto.RegionRequestDto;
import com.api.yirang.common.service.DistributionRegionService;
import com.api.yirang.common.service.RegionService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DistributionRegionServiceTest {

    // Test할 serivce
    @Autowired
    DistributionRegionService distributionRegionService;

    // 보조 Services
    @Autowired
    UserService userService;
    @Autowired
    RegionService regionService;
    @Autowired
    AdminService adminService;

    // Variables
    String regionNameFirst = "중구";
    String regionNameSecond = "수성구";
    String regionNameThird = "남구";

    long userIdFirst = 1468416139;
    long userIdSecond = 1466315491;
    long userIdThird = 1514779183;
    long userIdFourth = 1467656807;

    // helper
    private void deleteAdmin(Long userId){
        User user = userService.findUserByUserId(userId);
        if (adminService.isExistedUser(user)){
            userService.fireAdmin(userId);
        }
    }
    private void deleteRegion(String regionName){
        if (regionService.isExistedByRegionName(regionName)){
            regionService.deleteByRegionName(regionName);
        }
    }

    private void saveAdmin(Long userId){
        User user = userService.findUserByUserId(userId);
        if (!adminService.isExistedUser(user)){
            userService.registerAdmin(userId);
        }
    }

    private void saveRegion(String regionName){
        if(!regionService.isExistedByRegionName(regionName)){
            regionService.save(RegionRequestDto.builder().regionName(regionName).build());
        }
    }

    @Before
    public void setUp(){
        System.out.println("새로운 테스트 시작합니다.");
        saveAdmin(userIdFirst);
        saveAdmin(userIdSecond);
        saveAdmin(userIdThird);
        saveAdmin(userIdFourth);

        saveRegion(regionNameFirst);
        saveRegion(regionNameSecond);
        saveRegion(regionNameThird);
    }

    @After
    public void tearDown(){
        System.out.println("이번 테스트 끝났습니다.");
        deleteAdmin((userIdFirst));
        deleteAdmin((userIdSecond));
        deleteAdmin((userIdThird));
        deleteAdmin((userIdFourth));

        deleteRegion(regionNameFirst);
        deleteRegion(regionNameSecond);
        deleteRegion(regionNameThird);

        distributionRegionService.deleteAll();
    }

    @Test
    public void 정상적인_지역_배정하고_하나_조회하기() {
        distributionRegionService.save(userIdSecond, regionNameFirst);
        distributionRegionService.save(userIdSecond, regionNameSecond);

        distributionRegionService.save(userIdThird, regionNameThird);

        DistributionRegion distributionRegion1 =
                distributionRegionService.findDistributionByUserIdAndRegionName(userIdThird, regionNameThird);
        assertThat(distributionRegion1.getAdmin().getUser().getUserId()).isEqualTo(userIdThird);
        assertThat(distributionRegion1.getRegion().getRegionName()).isEqualTo(regionNameThird);

        DistributionRegion distributionRegion2 =
                distributionRegionService.findDistributionByUserIdAndRegionName(userIdSecond, regionNameFirst);
        assertThat(distributionRegion2.getAdmin().getUser().getUserId()).isEqualTo(userIdSecond);
        assertThat(distributionRegion2.getRegion().getRegionName()).isEqualTo(regionNameFirst);
    }

    @Test
    public void 정상적인_지역_배정하고_관리자아이디로_조회하기(){
        distributionRegionService.save(userIdSecond, regionNameFirst);
        distributionRegionService.save(userIdSecond, regionNameSecond);
        distributionRegionService.save(userIdSecond, regionNameThird);

        Collection<String> regions = new HashSet<>();
        regions.add(regionNameFirst);
        regions.add(regionNameSecond);
        regions.add(regionNameThird);

        Collection<DistributionRegion> distributionRegions =
                distributionRegionService.findDistributionsByUserId(userIdSecond);

        Iterator<DistributionRegion> itr = distributionRegions.iterator();
        while(itr.hasNext()){
            DistributionRegion distributionRegion = itr.next();
            assertThat(distributionRegion.getAdmin().getUser().getUserId()).isEqualTo(userIdSecond);
            assertTrue(regions.contains(distributionRegion.getRegion().getRegionName()));
        }
    }
    @Test
    public void 정상적인_지역_배정하고_지역이름으로_조회하기(){
        distributionRegionService.save(userIdFirst, regionNameFirst);
        distributionRegionService.save(userIdSecond, regionNameFirst);
        distributionRegionService.save(userIdThird, regionNameFirst);

        Collection<Long> userIds = new HashSet<>();
        userIds.add(userIdFirst);
        userIds.add(userIdSecond);
        userIds.add(userIdThird);

        Collection<DistributionRegion> distributionRegions =
                distributionRegionService.findDistributionsByRegionName(regionNameFirst);

        Iterator<DistributionRegion> itr = distributionRegions.iterator();
        while(itr.hasNext()){
            DistributionRegion distributionRegion = itr.next();
            assertThat(distributionRegion.getRegion().getRegionName()).isEqualTo(regionNameFirst);
            assertTrue(userIds.contains(distributionRegion.getAdmin().getUser().getUserId()));
        }

    }

    @Test(expected = DistributionRegionNullException.class)
    public void 배정된_적_없는_배정_조회하기(){
        distributionRegionService.findDistributionByUserIdAndRegionName(userIdFirst, regionNameFirst);
    }
    @Test(expected = DistributionRegionNullException.class)
    public void 배정된_적_없는_배정_관리자아이디로_조회하기(){
        distributionRegionService.save(userIdFirst, regionNameFirst);
        distributionRegionService.save(userIdFirst, regionNameSecond);

        // userIdSecond는 배정된 적 없음
        distributionRegionService.findDistributionsByUserId(userIdSecond);
    }

    @Test(expected = DistributionRegionNullException.class)
    public void 배정된_적_없는_배정_지역이름으로_조회하기(){
        distributionRegionService.save(userIdFirst, regionNameFirst);
        distributionRegionService.save(userIdSecond, regionNameSecond);

        // regionNameThird는 배정된 적 없음
        distributionRegionService.findDistributionsByRegionName(regionNameThird);

    }

    @Test(expected = AdminNullException.class)
    public void 관리자_없이_지역_배정하기(){
        // userId First라는 관리자는 이제 없음
        deleteAdmin(userIdFirst);

        distributionRegionService.save(userIdFirst, regionNameFirst);

    }

    @Test(expected = RegionNullException.class)
    public void 지역이름_없이_지역_배정하기(){
        // regionNameFirst는 이제 없음
        deleteRegion(regionNameFirst);

        distributionRegionService.save(userIdFirst, regionNameFirst);

    }
    @Test(expected = DistributionRegionNullException.class)
    public void 지역_배졍하고_관리자_지우기(){
        // 해당 관리자와 관련된 배정은 다 지워져야 합니다.

        // 저장
        distributionRegionService.save(userIdFirst, regionNameFirst);
        distributionRegionService.save(userIdFirst, regionNameSecond);
        distributionRegionService.save(userIdFirst, regionNameThird);

        // 저장된거 확인
        DistributionRegion distributionRegion1 =
                distributionRegionService.findDistributionByUserIdAndRegionName(userIdFirst, regionNameFirst);
        assertThat(distributionRegion1.getAdmin().getUser().getUserId()).isEqualTo(userIdFirst);
        assertThat(distributionRegion1.getRegion().getRegionName()).isEqualTo(regionNameFirst);

        // 삭제
        distributionRegionService.deleteAllByUserId(userIdFirst);
        distributionRegionService.findDistributionsByUserId(userIdFirst);

    }

    @Test(expected = DistributionRegionNullException.class)
    public void 지역_배정하고_지역_지우기(){
        // 해당 지역과 관련된 배정은 다 지워져야 합니다.

        distributionRegionService.save(userIdFirst, regionNameFirst);
        distributionRegionService.save(userIdSecond, regionNameFirst);
        distributionRegionService.save(userIdThird, regionNameFirst);

        // 저장된거 확인
        DistributionRegion distributionRegion1 =
                distributionRegionService.findDistributionByUserIdAndRegionName(userIdFirst, regionNameFirst);
        assertThat(distributionRegion1.getAdmin().getUser().getUserId()).isEqualTo(userIdFirst);
        assertThat(distributionRegion1.getRegion().getRegionName()).isEqualTo(regionNameFirst);

        // 삭제
        distributionRegionService.deleteAllByRegionName(regionNameFirst);
        distributionRegionService.findDistributionsByRegionName(regionNameFirst);
    }


    @Test
    public void 지역_배정하고_지역배정_지우기(){
        // 관리자와 지역은 아무 이상이 없어야 합니다.
        distributionRegionService.save(userIdFirst, regionNameFirst);
        distributionRegionService.save(userIdSecond, regionNameSecond);

        // 저장된거 확인
        DistributionRegion distributionRegion1 =
                distributionRegionService.findDistributionByUserIdAndRegionName(userIdFirst, regionNameFirst);
        assertThat(distributionRegion1.getAdmin().getUser().getUserId()).isEqualTo(userIdFirst);
        assertThat(distributionRegion1.getRegion().getRegionName()).isEqualTo(regionNameFirst);

        // 저장된거 확인
        DistributionRegion distributionRegion2 =
                distributionRegionService.findDistributionByUserIdAndRegionName(userIdSecond, regionNameSecond);
        assertThat(distributionRegion2.getAdmin().getUser().getUserId()).isEqualTo(userIdSecond);
        assertThat(distributionRegion2.getRegion().getRegionName()).isEqualTo(regionNameSecond);

        distributionRegionService.deleteByUserIdAndRegionName(userIdFirst,regionNameFirst);
        distributionRegionService.deleteByUserIdAndRegionName(userIdSecond,regionNameSecond);

        // 관리자  확인
        assertTrue(adminService.isExistedByUserId(userIdFirst));
        assertTrue(adminService.isExistedByUserId(userIdSecond));

        // 지역 확인
        assertTrue(regionService.isExistedByRegionName(regionNameFirst));
        assertTrue(regionService.isExistedByRegionName(regionNameSecond));
    }


}
