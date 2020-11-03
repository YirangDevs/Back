package com.api.yirang.notices.domain.notice.converter;

import com.api.yirang.auth.domain.user.model.Admin;
import com.api.yirang.common.support.time.TimeConverter;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.notices.domain.notice.model.Notice;
import com.api.yirang.notices.presentation.dto.NoticeOneResponseDto;
import com.api.yirang.notices.presentation.dto.NoticeResponseDto;

public class NoticeConverter {


    public static final Notice convertFromDtoToModel(String title, Admin admin, Activity activity)
    {
        return Notice.builder()
                     .title(title)
                     .admin(admin)
                     .activity(activity)
                     .build();
    }

    public static final NoticeOneResponseDto convertFromNoticeToOneResponse(Notice notice, Activity activity){
        String dtovString = TimeConverter.LocalDateTimeToString(activity.getDtov());
        String dtodString = TimeConverter.LocalDateTimeToString(activity.getDtod());
        String region = activity.getRegion().getRegionName();

        String[] dtovArray = dtovString.split(" ");
        String dov = dtovArray[0];
        String tov = dtovArray[1];

        String dod = dtodString.split(" ")[0];

        return NoticeOneResponseDto.builder()
                                   .id(notice.getNoticeId())
                                   .title(notice.getTitle()).content(activity.getContent())
                                   .nor(activity.getNor()).noa(activity.getNoa())
                                   .region(region)
                                   .dov(dov).tov(tov).dod(dod)
                                   .build();
    }

    public static final NoticeResponseDto convertFromNoticeToResponse(Notice notice, Activity activity){
        String dtov = TimeConverter.LocalDateTimeToString(activity.getDtov());

        String[] dtovArray = dtov.split(" ");
        String dov = dtovArray[0];
        String tov = dtovArray[1];

        return NoticeResponseDto.builder()
                                .id(notice.getNoticeId())
                                .title(notice.getTitle())
                                .nor(activity.getNor())
                                .dov(dov).tov(tov)
                                .build();
    }
}
