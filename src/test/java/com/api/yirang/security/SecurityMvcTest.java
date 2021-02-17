package com.api.yirang.security;

import com.api.yirang.auth.application.basicService.AdminService;
import com.api.yirang.auth.application.basicService.VolunteerBasicService;
import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.auth.domain.jwt.components.JwtProvider;
import com.api.yirang.auth.domain.user.model.Admin;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.domain.user.model.Volunteer;
import com.api.yirang.auth.generator.UserGenerator;
import com.api.yirang.auth.support.type.Authority;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by JeongminYoo on 2021/2/11
 * Work with Yirang
 * Email: likemin0142@gmail.com
 * Blog: http://Beewoom.github.io
 * Github: http://github.com/Biewoom
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test-security")
public class SecurityMvcTest {

    @Autowired
    private WebApplicationContext context;

    // make YAT
    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    UserService userService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private VolunteerBasicService volunteerBasicService;

    private MockMvc mvc;

    private final String baseURL = "http://localhost:8080/v1/apis";

    private String volunteerYAT;
    private String adminYAT;
    private String superAdminYAT;

    private Admin admin;
    private Admin superAdmin;
    private Volunteer volunteer;

    private Admin saveRandomAdmin(){
        User adminUser = UserGenerator.createRandomUser(Authority.ROLE_ADMIN);
        return adminService.save(adminUser);
    }

    private Admin saveRandomSuperAdmin(){
        User adminUser = UserGenerator.createRandomUser(Authority.ROLE_SUPER_ADMIN);
        return adminService.save(adminUser);
    }

    private Volunteer saveRandomVolunteer(){
        User volunteerUser = UserGenerator.createRandomUser(Authority.ROLE_VOLUNTEER);
        return volunteerBasicService.save(volunteerUser);
    }

    private void makePostRequest(String url, String YAT,
                                 Object object, ResultMatcher resultMatcher) throws Exception {

        if (object == null) {
            mvc.perform(MockMvcRequestBuilders
                                .post(url)
                                .header("Authorization", "Bearer " + YAT))
               .andExpect(resultMatcher);
        }
        else {
            mvc.perform(MockMvcRequestBuilders
                                .post(url)
                                .header("Authorization", "Bearer " + YAT)
                                .content(new Gson().toJson(object))
                                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(resultMatcher);
        }
    }
    private void makeGetRequest(String url, String YAT,
                             Object object, ResultMatcher resultMatcher) throws Exception {

        if (object == null) {
            mvc.perform(MockMvcRequestBuilders
                                .get(url)
                                .header("Authorization", "Bearer " + YAT))
               .andExpect(resultMatcher);
        }
        else {
            mvc.perform(MockMvcRequestBuilders
                                .get(url)
                                .header("Authorization", "Bearer " + YAT)
                                .content(new Gson().toJson(object))
                                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(resultMatcher);
        }
    }
    private void makePutRequest(String url, String YAT,
                             Object object, ResultMatcher resultMatcher) throws Exception{

        if (object == null){
            mvc.perform(MockMvcRequestBuilders
                                .put(url)
                                .header("Authorization", "Bearer " + YAT))
               .andExpect(resultMatcher);
        }
        else{
            mvc.perform(MockMvcRequestBuilders
                                .put(url)
                                .header("Authorization", "Bearer " + YAT)
                                .content(new Gson().toJson(object))
                                .contentType(MediaType.APPLICATION_JSON) )
               .andExpect(resultMatcher);
        }
    }

    private void makeDeleteRequest(String url, String YAT,
                             Object object, ResultMatcher resultMatcher) throws Exception{

        if (object == null){
            mvc.perform(MockMvcRequestBuilders
                                .delete(url)
                                .header("Authorization", "Bearer " + YAT))
               .andExpect(resultMatcher);
        }
        else{
            mvc.perform(MockMvcRequestBuilders
                                .delete(url)
                                .header("Authorization", "Bearer " + YAT)
                                .content(new Gson().toJson(object))
                                .contentType(MediaType.APPLICATION_JSON) )
               .andExpect(resultMatcher);
        }
    }

    @Before
    public void setUp(){
        mvc = MockMvcBuilders.webAppContextSetup(context)
                             .addFilter(((request, response, chain) -> {
                                            response.setCharacterEncoding("UTF-8");
                                            chain.doFilter(request, response); }))
                             .apply(springSecurity())
                             .build();

        String imageURL = "random";
        String email = "1234@naver.com";
        // make users
        admin = saveRandomAdmin();
        superAdmin = saveRandomSuperAdmin();
        volunteer = saveRandomVolunteer();

        // make YAT
        adminYAT = jwtProvider.generateJwtToken(admin.getUser().getUsername(), imageURL,
                                                admin.getUser().getUserId(), email);
        superAdminYAT = jwtProvider.generateJwtToken(superAdmin.getUser().getUsername(), imageURL,
                                                     superAdmin.getUser().getUserId(), email);
        volunteerYAT = jwtProvider.generateJwtToken(volunteer.getUser().getUsername(), imageURL,
                                                    volunteer.getUser().getUserId(), email);
    }



    @Test
    public void AdminController_전체유저_보여주는API_권한테스트() throws Exception {
        final String url = baseURL + "/admins/user-auths";

        makeGetRequest(url, volunteerYAT, null, status().isForbidden());
        makeGetRequest(url, adminYAT, null, status().isForbidden());
        makeGetRequest(url, superAdminYAT, null, status().isOk());
    }

    @Test
    public void AdminController_관리자승급_권한_테스트() throws Exception {
        Long userId = volunteer.getUser().getUserId();

        final String url = baseURL + "/admins/" + userId;

        makePostRequest(url, volunteerYAT, null, status().isForbidden());
        makePostRequest(url, superAdminYAT, null, status().isCreated());
    }

    @Test
    public void AdminController_관리자승급_권한테스트() throws Exception{
        Long userId = volunteer.getUser().getUserId();

        final String upgradeUrl = baseURL + "/admins/" + userId;
        final String regionUrl = baseURL + "/admins/region";

        // 업그레이드 하기 전
        makeGetRequest(regionUrl, volunteerYAT, null, status().isForbidden());

        // 업그레이드 후
        makePostRequest(upgradeUrl, superAdminYAT, null, status().isCreated());

        // 지역 보는 거 가능
        makeGetRequest(regionUrl, volunteerYAT, null, status().isOk());

        // 지역 머지?
        String response = mvc.perform(MockMvcRequestBuilders
                            .get(regionUrl)
                            .header("Authorization", "Bearer " + volunteerYAT))
           .andReturn().getResponse().getContentAsString();

        System.out.println("response: " + response);
    }
    @Test
    public void 관리자의_관리_지역_업데이트() throws Exception {
        Long userId = admin.getUser().getUserId();

        final String url = baseURL + "/admins/" + userId;

        List<String> regions = Arrays.asList("중구", "서구");

        makePutRequest(url, volunteerYAT, regions, status().isForbidden());
        makePutRequest(url, adminYAT, regions, status().isForbidden());
        makePutRequest(url, superAdminYAT, regions, status().isOk());

        String response = mvc.perform(MockMvcRequestBuilders
                                              .get(baseURL + "/admins/region")
                                              .header("Authorization", "Bearer " + adminYAT)
                                              .contentType(MediaType.APPLICATION_JSON)
                                        ).andReturn().getResponse().getContentAsString();

        System.out.println("response: " + response);
        assertThat(new Gson().fromJson(response, Map.class).get("regions")).isEqualTo(regions);
    }

    @Test
    public void 관리자_권한_다운그레이드() throws Exception {
        Long userId = admin.getUser().getUserId();

        final String downgradeUrl = baseURL + "/admins/" + userId;

        final String regionUrl = baseURL + "/admins/region";

        // Get
        makeGetRequest(regionUrl, adminYAT, null, status().isOk());

        // Super admin이 다운그레이드 시킴
        makeDeleteRequest(downgradeUrl, superAdminYAT, null, status().isNoContent());

        // 다시 접근하면 권한 금지
        makeGetRequest(regionUrl, adminYAT, null, status().isForbidden());

    }



}
