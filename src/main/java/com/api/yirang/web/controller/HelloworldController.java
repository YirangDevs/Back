package com.api.yirang.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloworldController {

    @GetMapping("/hello_world")
    public String helloTest(){
        return "Yirang Hello world! Don't give up! 우린 한다!";
    }
    @GetMapping("/please")
    public String pleaseTest(){
        return "Please I want to go home!";
    }
    @GetMapping("/complete")
    public String complete(){
        return "It is miracle!!";
    }
}
