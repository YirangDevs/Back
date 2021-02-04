package com.api.yirang.admin.services;

import com.api.yirang.auth.application.basicService.AdminService;
import com.api.yirang.auth.domain.user.model.Admin;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.domain.user.model.Volunteer;
import com.api.yirang.auth.generator.UserGenerator;
import com.api.yirang.auth.support.type.Authority;
import com.api.yirang.common.support.type.Region;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AdminServiceTest {
    @Autowired
    AdminService adminService;

    private Admin saveRandomAdmin(){
        User adminUser = UserGenerator.createRandomUser(Authority.ROLE_ADMIN);
        return adminService.save(adminUser);
    }

    @Test
    public void 구역_변경_테스트() {
        Admin admin = saveRandomAdmin();
        List<Region> regions = Arrays.asList(Region.CENTRAL_DISTRICT, Region.NORTH_DISTRICT,
                                             Region.DALSEO_DISTRICT, Region.EAST_DISTRICT);

        System.out.println("Before Update: " + adminService.findAdminByUserId(admin.getUser().getUserId()));
        adminService.updateRegionsByUserId(admin.getUser().getUserId(), regions);

        assertThat(adminService.findAreasByUserId(admin.getUser().getUserId())).isEqualTo(regions);

        System.out.println("After Update: " + adminService.findAdminByUserId(admin.getUser().getUserId()));
    }


}
