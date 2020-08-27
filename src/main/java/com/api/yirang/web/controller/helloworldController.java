package com.api.yirang.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class helloworldController {

    @GetMapping("/hello_world")
    public String helloTest(){
        return "Yirange Hello world!";
    }
}
