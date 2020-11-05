package com.api.yirang.senior.IntegrationTesting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SeniorControllerTest {

    String url = "http://localhost:8080" + "/v1/apis/seniors";

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    // variable
    Map<String, String> mmap;


    @Before
    public void setup(){
        mvc = MockMvcBuilders.webAppContextSetup(context)
                             .apply(springSecurity())
                             .build();
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



}
