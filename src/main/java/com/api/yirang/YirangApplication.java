package com.api.yirang;

import com.api.yirang.common.support.custom.MyPrintStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class YirangApplication {
    public static void main(String[] args) {
        System.setOut(new MyPrintStream(System.out));
        SpringApplication.run(YirangApplication.class, args);
    }

}
