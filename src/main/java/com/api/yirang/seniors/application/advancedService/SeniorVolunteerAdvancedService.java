package com.api.yirang.seniors.application.advancedService;


import com.api.yirang.seniors.application.basicService.SeniorBasicService;
import com.api.yirang.seniors.application.basicService.VolunteerBasicService;
import com.api.yirang.seniors.domain.senior.model.Senior;
import com.api.yirang.seniors.presentation.dto.RegisterSeniorRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
@RequiredArgsConstructor
public class SeniorVolunteerAdvancedService {

    private final SeniorBasicService seniorBasicService;
    private final VolunteerBasicService volunteerBasicService;

    public void registerSenior(@Valid RegisterSeniorRequestDto registerSeniorRequestDto) {

    }
}
