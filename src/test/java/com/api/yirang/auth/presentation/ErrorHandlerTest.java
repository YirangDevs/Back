package com.api.yirang.auth.presentation;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class ErrorHandlerTest {

    @Autowired
    MockMvc mockMvc;

    // Mock variables
    String kakaoAccessToken = "LboVsNsQ8Zr4KSuSq_OWiWevabmArYZ2RPMUjAo9dZsAAAF0c4wmog";

    @Test
    public void 만료된_카카오_토큰으로_로그인하기() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/apis/auth/signin")
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .header("Authorization", "Bearer " + kakaoAccessToken))
           .andExpect(status().isForbidden());
    }

}
