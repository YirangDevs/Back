package com.api.yirang.seniors.domain.senior.converter;

import com.api.yirang.common.domain.region.model.Region;
import com.api.yirang.common.support.type.Sex;
import com.api.yirang.seniors.domain.senior.model.Senior;
import com.api.yirang.seniors.presentation.dto.request.RegisterSeniorRequestDto;

public class SeniorConverter {


    public static final Senior convertFromDtoToModel(RegisterSeniorRequestDto registerSeniorRequestDto,
                                                     Region region){
        String name = registerSeniorRequestDto.getName();
        String address = registerSeniorRequestDto.getAddress();
        String phone = registerSeniorRequestDto.getPhone();
        Sex sex = registerSeniorRequestDto.getSex();

        return Senior.builder()
                     .seniorName(name).sex(sex)
                     .address(address).phone(phone)
                     .region(region)
                     .build();
    }
}
