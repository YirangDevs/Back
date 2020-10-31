package com.api.yirang.notice.controller;

import com.api.yirang.notices.presentation.dto.NoticeRegisterRequestDto;
import com.api.yirang.notices.presentation.dto.embeded.ActivityRegisterRequestDto;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:properties/application-test.properties")
public class NoticeControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    //variables
    String url = "http://localhost:8080" + "/v1/apis/manage/notices";
    @Value("${test.api.token.yirang_access_token}")
    String YAT;

    @Before
    public void setup(){
        mvc = MockMvcBuilders.webAppContextSetup(context)
                             .apply(springSecurity())
                             .build();

    }
    private void makeOk(String content, Long nor,
                                String dov, String tov, String dod,
                                String region, String title) throws Exception {

        ActivityRegisterRequestDto activityRegisterRequestDto =
                ActivityRegisterRequestDto.builder()
                                          .content(content)
                                          .nor(nor)
                                          .dov(dov).tov(tov)
                                          .dod(dod)
                                          .region(region)
                                          .build();
        NoticeRegisterRequestDto noticeRegisterRequestDto =
                NoticeRegisterRequestDto.builder()
                                        .title(title)
                                        .activityRegisterRequestDto(activityRegisterRequestDto)
                                        .build();
        System.out.println(noticeRegisterRequestDto);

        mvc.perform(MockMvcRequestBuilders
                            .post(url)
                            .header("Authorization", "Bearer " + YAT)
                            .content(new Gson().toJson(noticeRegisterRequestDto))
                            .contentType(MediaType.APPLICATION_JSON) )
           .andExpect(status().isCreated());
    }

    private void makeBadRequest(String content, Long nor,
                                String dov, String tov, String dod,
                                String region, String title) throws Exception {

        ActivityRegisterRequestDto activityRegisterRequestDto =
                ActivityRegisterRequestDto.builder()
                                          .content(content)
                                          .nor(nor)
                                          .dov(dov).tov(tov)
                                          .dod(dod)
                                          .region(region)
                                          .build();
        NoticeRegisterRequestDto noticeRegisterRequestDto =
                NoticeRegisterRequestDto.builder()
                                        .title(title)
                                        .activityRegisterRequestDto(activityRegisterRequestDto)
                                        .build();
        System.out.println(noticeRegisterRequestDto);

        mvc.perform(MockMvcRequestBuilders
                            .post(url)
                            .header("Authorization", "Bearer " + YAT)
                            .content(new Gson().toJson(noticeRegisterRequestDto))
                            .contentType(MediaType.APPLICATION_JSON) )
           .andExpect(status().isBadRequest());
    }

    @Test
    public void 정상적인_DTO_Valid_테스트() throws Exception {
        makeOk("1234", Long.valueOf(5), "2020-01-23", "23:55:55",
               "2020-02-23", "수성구", "Good");
    }

    @Test
    public void Tile가_이상한_인테스트() throws Exception {
        makeBadRequest("1234", Long.valueOf(5), "2020-01-23", "23:55:55",
                       "2020-02-23", "수성구", "G");
    }

    @Test
    public void NOR가_이상한_테스트() throws Exception {
        makeBadRequest("1234", Long.valueOf(-1), "2020-01-23", "23:55:55",
                       "2020-02-23", "수성구", "GOOD");
    }

    @Test
    public void DOV가_이상한_테스트() throws Exception {
        makeBadRequest("1234", Long.valueOf(5), "2020230123", "23:55:55",
                       "2020-02-23", "수성구", "Good");
    }
    @Test
    public void TOV가_이상한_테스트() throws Exception {
        makeBadRequest("1234", Long.valueOf(5), "2020-01-23", "23675:55",
                       "2020-02-23", "수성구", "Good");
    }

    @Test
    public void DOD가_이상한_테스트() throws Exception {
        makeBadRequest("1234", Long.valueOf(5), "2020-01-23", "23:55:55",
                       "2020-02-1233", "수성구", "Good");
    }

}
