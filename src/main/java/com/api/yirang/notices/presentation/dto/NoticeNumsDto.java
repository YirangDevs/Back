package com.api.yirang.notices.presentation.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@Builder
public class NoticeNumsDto {

    private final Long totalNoticeNums;

    public NoticeNumsDto() {
        this.totalNoticeNums = null;
    }
}
