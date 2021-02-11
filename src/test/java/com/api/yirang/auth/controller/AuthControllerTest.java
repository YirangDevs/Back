package com.api.yirang.auth.controller;

import com.api.yirang.auth.application.advancedService.AuthService;
import com.api.yirang.auth.application.basicService.AdminService;
import com.api.yirang.auth.application.basicService.VolunteerBasicService;
import com.api.yirang.auth.application.intermediateService.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

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
public class AuthControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    // YAT generator
    @Autowired
    private AuthService authService;

    // Data 만드는 Services
    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private VolunteerBasicService volunteerBasicService;
    //
}
