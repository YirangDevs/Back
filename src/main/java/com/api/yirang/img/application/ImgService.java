package com.api.yirang.img.application;


import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.img.repository.ImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImgService {


    // DI services
    private final UserService userService;


    // DI Dao
    private final ImgRepository imgRepository;




}
