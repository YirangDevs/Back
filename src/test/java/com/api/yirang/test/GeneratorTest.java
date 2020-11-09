package com.api.yirang.test;

import com.api.yirang.common.generator.TimeGenerator;
import org.junit.Test;

public class GeneratorTest {

    @Test
    public void LocalDateTime_생성자_테스트(){
        for (int i = 0; i < 10; i++){
            System.out.println(TimeGenerator.generateRandomLocalDateTime());
        }
    }
}
