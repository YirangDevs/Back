package com.api.yirang.auth.application;

import com.api.yirang.auth.application.advancedService.AuthService;
import com.api.yirang.auth.presentation.dto.SignInRequestDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthServiceTest {

    @Autowired
    AuthService authService;

    @Test
    public void Sign_in_테스트_하기(){
        return;
    }
}
