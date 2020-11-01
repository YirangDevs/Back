package com.api.yirang.seniors.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class SeniorController {

    // DI Serivce
    private final SeniorAdvancedService seniorAdvancedService;

    @PostMapping
    @

}
