package com.api.yirang.seniors.domain.senior.converter;

import com.api.yirang.common.support.type.Region;
import com.api.yirang.common.support.type.Sex;
import com.api.yirang.seniors.domain.senior.model.Senior;
import com.api.yirang.seniors.presentation.dto.request.RegisterSeniorRequestDto;

public class SeniorConverter {

    public static Senior convertFromDtoToModel(RegisterSeniorRequestDto registerSeniorRequestDto){
        String name = registerSeniorRequestDto.getName();
        String address = registerSeniorRequestDto.getAddress();
        String phone = registerSeniorRequestDto.getPhone();
        Sex sex = registerSeniorRequestDto.getSex();
        Region region = registerSeniorRequestDto.getRegion();

        return Senior.builder()
                     .seniorName(name).sex(sex)
                     .address(address).phone(phone)
                     .region(region)
                     .build();
    }
}
