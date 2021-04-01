package com.api.yirang.apply.presentation.dto;

import com.api.yirang.apply.support.type.MatchingState;
import com.api.yirang.common.support.type.Region;
import lombok.*;

/**
 * Created by JeongminYoo on 2021/1/6
 * Work with Yirang
 * Email: likemin0142@gmail.com
 * Blog: http://Beewoom.github.io
 * Github: http://github.com/Biewoom
 */

@Getter
@ToString
@AllArgsConstructor
@Builder
public class ApplyResponseDto {

    // Fields
    private final Long applyId; // Apply ID
    private final MatchingState matchingState;
    private final String dtoa; // 신청한 날짜/시간

    private final String dtov; // 봉사 날짜/시간
    private final Region region; // 봉사 지역
    private final Long activityId; // noticeId

    public ApplyResponseDto() {
        this.applyId = null;
        this.dtoa = null;
        this.dtov = null;
        this.region = null;
        this.matchingState = null;
        this.activityId = null;
    }
}
