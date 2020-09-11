package com.api.yirang.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloworldController {

    @GetMapping("/hello_world")
    public String helloTest(){
        System.out.println("HelloTEst executing");
        return "Yirang Hello world! Don't give up! 우린 한!";
    }
    @GetMapping("/please")
    public String pleaseTest(){
        return "Please I want to go home!";
    }
    @GetMapping("/complete")
    public String complete(){
        System.out.println("completeTEst executing");
        return "It is miracle!!";
    }
    @GetMapping("/yongwon")
    public String getYoung(){
        return "빨리 Deploy 하란 말이야!";
    }
    @GetMapping("/gohome")
    public String goHome(){return "집못감";}
}
