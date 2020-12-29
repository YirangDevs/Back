package com.api.yirang.apply.application;

import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.notices.application.advancedService.NoticeActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * Created by JeongminYoo on 2020/12/30
 * Work with Yirang
 * Email: likemin0142@gmail.com
 * Blog: http://Beewoom.github.io
 * Github: http://github.com/Biewoom
 */

@Service
@RequiredArgsConstructor
@Transactional
public class ApplyAdvancedService {

    // DI basic services [In this Feature]
    private final ApplyBasicService applyBasicService;

    // DI basic service [In other Features]
    private final UserService userService;
    private final NoticeActivityService noticeActivityService;

    /** TO-DO
     * 봉사자들 신청 목록에는 어떤 것들이 들어가?
     **/
    public Collection getVolunteersFromNoticeId(Long noticeId) {
        return null;
    }

    /** TO-DO
     * 봉사자들이
     */

}
