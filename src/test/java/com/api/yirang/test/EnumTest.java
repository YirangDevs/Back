package com.api.yirang.test;


import com.api.yirang.common.support.type.Sex;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EnumTest {

    @Test
    public void 동등_테스트(){
        Sex a = Sex.SEX_FEMALE;
        Sex b = Sex.SEX_FEMALE;

        assertTrue(a == b);
    }
    @Test
    public void 비동등_테스트(){
        Sex a = Sex.SEX_FEMALE;
        Sex b = Sex.SEX_FEMALE;

        assertFalse(a != b);
    }
}
