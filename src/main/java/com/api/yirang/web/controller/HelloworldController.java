package com.api.yirang.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloworldController {

    @GetMapping("/hello_world")
    public String helloTest(){
        return "Yirang Hello world! Don't give up! 우린 한!";
    }
    @GetMapping("/please")
    public String pleaseTest(){
        return "Please I want to go home!";
    }
    @GetMapping("/complete")
    public String complete(){
        return "It is miracle!!";
    }
    @GetMapping("/yongwon")
    public String getYoung(){
        return "빨리 Deploy 하란 말이야!";
    }
    @GetMapping("/gohome")
    public String goHome(){return "집못감";}
}
