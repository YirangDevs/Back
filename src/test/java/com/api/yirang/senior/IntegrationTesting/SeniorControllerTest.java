package com.api.yirang.senior.IntegrationTesting;

import com.api.yirang.common.generator.EnumGenerator;
import com.api.yirang.common.generator.NumberRandomGenerator;
import com.api.yirang.common.generator.StringRandomGenerator;
import com.api.yirang.common.support.type.Region;
import com.api.yirang.common.support.type.Sex;
import com.api.yirang.notice.generator.ActivityGenerator;
import com.api.yirang.notices.application.basicService.ActivityBasicService;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.notices.presentation.dto.embeded.ActivityRegisterRequestDto;
import com.api.yirang.notices.repository.persistence.maria.ActivityDao;
import com.api.yirang.seniors.presentation.dto.request.RegisterTotalSeniorRequestDto;
import com.api.yirang.seniors.repository.persistence.maria.SeniorDao;
import com.api.yirang.seniors.repository.persistence.maria.VolunteerServiceDao;
import com.api.yirang.seniors.support.custom.ServiceType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class SeniorControllerTest {

    String url = "http://localhost:8080" + "/v1/apis/seniors";

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private SeniorDao seniorDao;

    @Autowired
    private ActivityDao activityDao;

    @Autowired
    private ActivityBasicService activityBasicService;

    @Autowired
    private VolunteerServiceDao volunteerServiceDao;

    private MockMvc mvc;

    // variable
    Map<String, String> mmap;
    List<RegisterTotalSeniorRequestDto> llist;

    private void makeMmap(){
        mmap = new HashMap<>();
        // variable
        mmap.put("name", "김복동");
        mmap.put("region", "중구");
        mmap.put("address", "집 근처");
        mmap.put("phone", "01055172058");
        mmap.put("sex", "female");
        mmap.put("type", "work");
        mmap.put("date", "2020-02-01");
        mmap.put("priority", "4");
        mmap.put("numsOfRequiredVolunteers", "2");
    }

    private RegisterTotalSeniorRequestDto makeRightRegisterTotalSeniorRequestDto(){
        return RegisterTotalSeniorRequestDto.builder()
                                            .name(StringRandomGenerator.generateKoreanNameWithLength(Long.valueOf(3)))
                                            .sex(EnumGenerator.generateRandomSex()).region(Region.SOOSEONG_DISTRICT)
                                            .priority(NumberRandomGenerator.generateLongValueWithRange(1 , 10))
                                            .type(EnumGenerator.generateRandomServiceType())
                                            .address(StringRandomGenerator.generateRandomKoreansWithLength(Long.valueOf(10)))
                                            .phone(StringRandomGenerator.generateNumericStringWithLength(Long.valueOf(9)))
                                            .date("2020-11-15")
                                            .build();
    }

    @Before
    public void setup(){
        mvc = MockMvcBuilders.webAppContextSetup(context)
                             .apply(springSecurity())
                             .build();
        makeMmap();

        llist = new ArrayList<>();
        for(int i =0; i < 10; i++){
            llist.add(makeRightRegisterTotalSeniorRequestDto());
        }
    }

    @After
    public void tearDown(){
        volunteerServiceDao.deleteAll();
        seniorDao.deleteAll();
        activityDao.deleteAll();
    }


    @Test
    public void Create_받기() throws Exception {
        LocalDateTime dtov = LocalDateTime.of(2020, 2, 1, 2, 11, 11);
        Activity activity = ActivityGenerator.createRandomActivity(Region.CENTRAL_DISTRICT, dtov);
        activityDao.save(activity);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(url)
                                          .content(new Gson().toJson(mmap))
                                          .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void Sex_이상하게_줌() throws Exception {
        mmap.put("sex", "man");
        mvc.perform(MockMvcRequestBuilders.post(url)
                                          .content(new Gson().toJson(mmap))
                                          .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());

    }
    @Test
    public void ServiceType_이상하게_줌() throws Exception {
        mmap.put("type", "노력봉사");
        mvc.perform(MockMvcRequestBuilders.post(url)
                                          .content(new Gson().toJson(mmap))
                                          .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }
    @Test
    public void 여러가지_이상하게_줌() throws Exception{
        mmap.put("sex", "man");
        mmap.put("type", "노력봉사");
        mmap.put("phone", "abc01055172058");
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(url)
                                                    .content(new Gson().toJson(mmap))
                                                    .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest()).andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    // check Test
    @Test
    public void 정상적으로_엑셀주기() throws Exception{
        String content = new JacksonJsonProvider().toJson(llist);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(url + "/check")
                                                                .content(content)
                                                                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
    }
    @Test
    public void 형식이_이상할_때_1개_Phone_date() throws Exception{
        llist.add(RegisterTotalSeniorRequestDto.builder()
                                               .name(StringRandomGenerator.generateKoreanNameWithLength(Long.valueOf(3)))
                                               .sex(EnumGenerator.generateRandomSex()).region(Region.SOOSEONG_DISTRICT)
                                               .priority(NumberRandomGenerator.generateLongValueWithRange(1 , 10))
                                               .type(EnumGenerator.generateRandomServiceType())
                                               .address(StringRandomGenerator.generateRandomKoreansWithLength(Long.valueOf(10)))
                                               .phone("123ssdbc")
                                               .date("2020-1115")
                                               .build());
        String content = new JacksonJsonProvider().toJson(llist);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(url + "/check")
                                                                .content(content)
                                                                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest()).andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString() );
    }
    @Test
    public void 형식이_이상할_때_2개() throws Exception{
        llist.add(RegisterTotalSeniorRequestDto.builder()
                                               .name(StringRandomGenerator.generateKoreanNameWithLength(Long.valueOf(3)))
                                               .sex(EnumGenerator.generateRandomSex()).region(Region.SOOSEONG_DISTRICT)
                                               .priority(NumberRandomGenerator.generateLongValueWithRange(1 , 10))
                                               .type(EnumGenerator.generateRandomServiceType())
                                               .address(StringRandomGenerator.generateRandomKoreansWithLength(Long.valueOf(10)))
                                               .phone("0105518239")
                                               .date("2020-1115")
                                               .build());
        llist.add(RegisterTotalSeniorRequestDto.builder()
                                               .name(StringRandomGenerator.generateKoreanNameWithLength(Long.valueOf(3)))
                                               .sex(EnumGenerator.generateRandomSex()).region(Region.SOOSEONG_DISTRICT)
                                               .priority(NumberRandomGenerator.generateLongValueWithRange(1 , 10))
                                               .type(EnumGenerator.generateRandomServiceType())
                                               .address(StringRandomGenerator.generateRandomKoreansWithLength(Long.valueOf(10)))
                                               .phone("010596969")
                                               .date("201115")
                                               .build());
        String content = new JacksonJsonProvider().toJson(llist);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(url + "/check")
                                                                .content(content)
                                                                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest()).andReturn();
        System.out.println("Response: " + mvcResult.getResponse().getContentAsString() );
    }

    @Test
    public void 빈_컨텐츠_일때() throws Exception{
        llist.clear();
        String content = new JacksonJsonProvider().toJson(llist);
        System.out.println("Content: " + content);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(url + "/check")
                                                                .content(content)
                                                                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest()).andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString() );
    }
    @Test
    public void 날짜가_다른_경우() throws Exception{
        llist.add(RegisterTotalSeniorRequestDto.builder()
                                               .name(StringRandomGenerator.generateKoreanNameWithLength(Long.valueOf(3)))
                                               .sex(EnumGenerator.generateRandomSex()).region(Region.SOOSEONG_DISTRICT)
                                               .priority(NumberRandomGenerator.generateLongValueWithRange(1 , 10))
                                               .type(EnumGenerator.generateRandomServiceType())
                                               .address(StringRandomGenerator.generateRandomKoreansWithLength(Long.valueOf(10)))
                                               .phone(StringRandomGenerator.generateNumericStringWithLength(Long.valueOf(9)))
                                               .date("2020-11-16")
                                               .build());
        String content = new JacksonJsonProvider().toJson(llist);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(url + "/check")
                                                                .content(content)
                                                                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest()).andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString() );
    }
    @Test
    public void 지역이_다른_경우() throws Exception{
        llist.add(RegisterTotalSeniorRequestDto.builder()
                                               .name(StringRandomGenerator.generateKoreanNameWithLength(Long.valueOf(3)))
                                               .sex(EnumGenerator.generateRandomSex()).region(Region.DALSEO_DISTRICT)
                                               .priority(NumberRandomGenerator.generateLongValueWithRange(1 , 10))
                                               .type(EnumGenerator.generateRandomServiceType())
                                               .address(StringRandomGenerator.generateRandomKoreansWithLength(Long.valueOf(10)))
                                               .phone(StringRandomGenerator.generateNumericStringWithLength(Long.valueOf(9)))
                                               .date("2020-11-15")
                                               .build());
        String content = new JacksonJsonProvider().toJson(llist);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(url + "/check")
                                                                .content(content)
                                                                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest()).andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString() );
    }
    @Test
    public void Request중에_중복된_데이터() throws Exception{
        llist.add(RegisterTotalSeniorRequestDto.builder()
                                               .name("김복자")
                                               .sex(Sex.SEX_FEMALE).region(Region.SOOSEONG_DISTRICT)
                                               .priority(Long.valueOf(1))
                                               .type(ServiceType.SERVICE_TALK)
                                               .address("이곡동")
                                               .phone("01055172058")
                                               .date("2020-11-15")
                                               .build());
        llist.add(RegisterTotalSeniorRequestDto.builder()
                                               .name("김복자")
                                               .sex(Sex.SEX_FEMALE).region(Region.SOOSEONG_DISTRICT)
                                               .priority(Long.valueOf(1))
                                               .type(ServiceType.SERVICE_TALK)
                                               .address("이곡동")
                                               .phone("01055172058")
                                               .date("2020-11-15")
                                               .build());
        String content = new JacksonJsonProvider().toJson(llist);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(url + "/check")
                                                                .content(content)
                                                                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest()).andReturn();
        System.out.println("Response: " + mvcResult.getResponse().getContentAsString() );
    }
    @Test
    public void 기존_데이터와_중복된_거_체크() throws Exception{
        String content = new JacksonJsonProvider().toJson(llist);
        MvcResult mvcResultFirst = mvc.perform(MockMvcRequestBuilders.post(url + "/check")
                                                                .content(content)
                                                                .contentType(MediaType.APPLICATION_JSON))
                                 .andExpect(status().isOk()).andReturn();
        // make activity
        LocalDateTime dtov = LocalDateTime.of(2020, 11, 15, 2, 11, 11);
        Activity activity = ActivityGenerator.createRandomActivity(Region.SOOSEONG_DISTRICT, dtov);
        System.out.println("Activity: " + activity);
        activityBasicService.save(activity);

        // excel 저장
        MvcResult mvcResultTotal = mvc.perform(MockMvcRequestBuilders.post(url + "/total")
                                          .content(content)
                                          .contentType(MediaType.APPLICATION_JSON))
                                      .andExpect(status().isCreated()).andReturn();
        // 다시 보내기
        MvcResult mvcResultSecond = mvc.perform(MockMvcRequestBuilders.post(url + "/check")
                                                                      .content(content)
                                                                      .contentType(MediaType.APPLICATION_JSON))
                                       .andExpect(status().isBadRequest()).andReturn();
        System.out.println("Response: " + mvcResultSecond.getResponse().getContentAsString() );
    }

}
