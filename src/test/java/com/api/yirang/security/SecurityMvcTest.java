package com.api.yirang.security;

import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.auth.domain.jwt.components.JwtProvider;
import com.api.yirang.auth.domain.user.model.Admin;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.domain.user.model.Volunteer;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

/**
 * Created by JeongminYoo on 2021/2/11
 * Work with Yirang
 * Email: likemin0142@gmail.com
 * Blog: http://Beewoom.github.io
 * Github: http://github.com/Biewoom
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class SecurityMvcTest {

    @Autowired
    private WebApplicationContext context;

    // make YAT
    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserService userService;

    private MockMvc mvc;

    private final String baseURL = "http://localhost:8080";

    private String volunteerYAT;
    private String adminYAT;
    private String superAdminYAT;

    private User user;
    private Admin admin;
    private Volunteer volunteer;

    @Before
    public void setUp(){
        mvc = MockMvcBuilders.webAppContextSetup(context)
                             .apply(springSecurity())
                             .build();


    }



}
