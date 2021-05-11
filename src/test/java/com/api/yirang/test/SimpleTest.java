package com.api.yirang.test;

import com.api.yirang.email.util.MailContentConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.LocalDateTime;

@RunWith(JUnit4.class)
public class SimpleTest {

    @Test
    public void 요일_구하기(){

        LocalDateTime now = LocalDateTime.now();
        System.out.println("Day: " + MailContentConverter.dayOfWeeksToKoreanContentString(now.getDayOfWeek().getValue()));
        System.out.println("Day: " + String.valueOf(now.getHour()+13));
    }
}
