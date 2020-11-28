package com.api.yirang.senior.IntegrationTesting;

import com.api.yirang.common.generator.EnumGenerator;
import com.api.yirang.common.generator.NumberRandomGenerator;
import com.api.yirang.common.generator.StringRandomGenerator;
import com.api.yirang.common.support.type.Region;
import com.api.yirang.seniors.presentation.dto.request.RegisterTotalSeniorRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class SeniorControllerTest {

    String url = "http://localhost:8080" + "/v1/apis/seniors";

    @Autowired
    private WebApplicationContext context;

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


    @Test
    public void OK받기() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(url)
                                          .content(new Gson().toJson(mmap))
                                          .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated());
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

    @Test
    public void 정상적으로_엑셀주기() throws Exception{
        String content = new JacksonJsonProvider().toJson(llist);
        System.out.println("Content: " + content);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(url + "/check")
                                                                .content(content)
                                                                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
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
        System.out.println("Content: " + content);
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
    public void 형식이_이상할_때() throws Exception{
        llist.add(RegisterTotalSeniorRequestDto.builder()
                                               .name(StringRandomGenerator.generateKoreanNameWithLength(Long.valueOf(3)))
                                               .sex(EnumGenerator.generateRandomSex()).region(Region.DALSEO_DISTRICT)
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

}
