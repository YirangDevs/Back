package com.api.yirang.notices.application.advancedService;

import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.auth.domain.jwt.components.JwtParser;
import com.api.yirang.auth.support.utils.ParsingHelper;
import com.api.yirang.common.service.RegionService;
import com.api.yirang.notices.application.basicService.ActivityService;
import com.api.yirang.notices.application.basicService.NoticeService;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.notices.presentation.dto.NoticeRegisterRequestDto;
import com.api.yirang.notices.presentation.dto.embeded.ActivityRegisterRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeActivityService {

    // DI service
    private final NoticeService noticeService;
    private final ActivityService activityService;

    // DI user Service
    private final UserService userservice;
    private final RegionService regionService;

    // DI JwtParser
    private final JwtParser jwtParser;

    @Transactional
    public void register(String header, NoticeRegisterRequestDto noticeRequestDto) {
        // Notice에 받아서 저장

        // header 뜯어서 누구인지 판단하고
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        AdminS

        // Title
        String title = noticeRequestDto.getTitle();


        // Dto를 받아서 뜯어서 -> Notice랑 Activity로 나누어야 함
        ActivityRegisterRequestDto activityRegisterRequestDto =
                noticeRequestDto.getActivityRegisterRequestDto();

        // Activity 받아서 DataBase에 저장
        Activity activity = Activity.builder().build();

    }

    public void
}
