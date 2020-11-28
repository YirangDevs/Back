package com.api.yirang.senior.unitTesting;

import com.api.yirang.auth.domain.jwt.components.JwtParser;
import com.api.yirang.common.generator.EnumGenerator;
import com.api.yirang.common.generator.NumberRandomGenerator;
import com.api.yirang.common.generator.StringRandomGenerator;
import com.api.yirang.common.support.custom.ValidCollection;
import com.api.yirang.common.support.type.Region;
import com.api.yirang.seniors.application.advancedService.SeniorVolunteerAdvancedService;
import com.api.yirang.seniors.presentation.controller.SeniorController;
import com.api.yirang.seniors.presentation.dto.request.RegisterTotalSeniorRequestDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.BindingResultUtils;

import static org.mockito.Mockito.when;

/**
 * Created by JeongminYoo on 2020/11/28
 * Work with Yirang
 * Email: likemin0142@gmail.com
 * Blog: http://Beewoom.github.io
 * Github: http://github.com/Biewoom
 */

@RunWith(MockitoJUnitRunner.class)
public class SeniorControllerUnitTest {

    // Test할 Component
    @InjectMocks
    SeniorController seniorController;

    @Mock
    SeniorVolunteerAdvancedService seniorVolunteerAdvancedService;

    @Mock
    JwtParser jwtParser;

    // Mock variable
    static final long LENGTH = 10;
    ValidCollection<RegisterTotalSeniorRequestDto> registerTotalSeniorRequestDtos = new ValidCollection<>();

    private RegisterTotalSeniorRequestDto makeRightRegisterTotalSeniorRequestDto(){
        return RegisterTotalSeniorRequestDto.builder()
                                            .name(StringRandomGenerator.generateKoreanNameWithLength(Long.valueOf(3)))
                                            .sex(EnumGenerator.generateRandomSex()).region(Region.SOOSEONG_DISTRICT)
                                            .priority(NumberRandomGenerator.generateLongValueWithRange(1 , 10))
                                            .type(EnumGenerator.generateRandomServiceType())
                                            .address(StringRandomGenerator.generateRandomKoreansWithLength(Long.valueOf(10)))
                                            .phone(StringRandomGenerator.generateNumericStringWithLength(Long.valueOf(9)))
                                            .date("2020-11-15")
                                            .build();
    }

    @Before
    public void setUp(){
        for(int i = 0; i < LENGTH; i++){
            registerTotalSeniorRequestDtos.add(makeRightRegisterTotalSeniorRequestDto());
        }
    }

    @Test
    public void 엑셀_파일_모든_파일이_올바른_경우(){
        when(seniorVolunteerAdvancedService.checkSameDateAndSameRegion(registerTotalSeniorRequestDtos)).thenReturn(true);
    }


}
