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
        return "Please I want to go home!";
    }
    @GetMapping("/complete")
    public String complete(){
        return "It is miracle!!";
    }
    @GetMapping("/yeonu")
    public String Yeonu(){
        return "I'm very very adorable!!!!";
    }
    @GetMapping("/test")
    public String test(){
        return "TESTING";
    }

}
