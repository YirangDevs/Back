package com.api.yirang.email.application;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AutomaticEmailService {

    @Scheduled(cron = "*/1 * * * * *")
    public void run(){
        System.out.println("Hello!");
    }
}
