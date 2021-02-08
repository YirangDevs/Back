package com.api.yirang.admin.controller;


import com.api.yirang.auth.application.basicService.AdminService;
import com.api.yirang.auth.domain.user.model.Admin;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.generator.UserGenerator;
import com.api.yirang.auth.support.type.Authority;
import com.api.yirang.common.generator.EnumGenerator;
import com.api.yirang.common.support.type.Region;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class AdminControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AdminService adminService;

    private Admin saveRandomAdmin(){
        User adminUser = UserGenerator.createRandomUser(Authority.ROLE_ADMIN);
        return adminService.save(adminUser);
    }

    private String url = "/v1/apis/admins/";

    @Test
    public void 올바른_형식의_지역_업데이트() throws Exception {
        Admin admin = saveRandomAdmin();
        Long userId = admin.getUser().getUserId();

        List<String> regions = Arrays.asList("중구","달성군","남구","수성구");
        List<Region> regionsList = Arrays.asList(Region.CENTRAL_DISTRICT, Region.DALSEONGGUN_DISTRICT,
                Region.SOUTH_DISTRICT, Region.SOOSEONG_DISTRICT);


        System.out.println("Regions: " + regions);

        mockMvc.perform(
                MockMvcRequestBuilders.put(url+userId)
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(new Gson().toJson(regions))
        ).andExpect(status().isOk());

        assertThat(adminService.findAreasByUserId(userId)).isEqualTo(regionsList);
    }

    @Test
    public void 이상한_형식의_지역_업데이트() throws Exception {
        Admin admin = saveRandomAdmin();
        Long userId = admin.getUser().getUserId();

        List<String> regions = Arrays.asList("민지","달서구","중구");

        System.out.println("Regions: " + regions);

        mockMvc.perform(
                MockMvcRequestBuilders.put(url+userId)
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(new Gson().toJson(regions))
        ).andExpect(status().isBadRequest());
    }
}
