package com.api.yirang.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloworldController {

    @GetMapping("/hello_world")
    public String helloTest(){
        return "Yirang Hello world!";
    }
    @GetMapping("/please")
    public String pleaseTest(){
        return "Please I want YOU!";
    }
}
