package com.api.yirang.notices.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@Builder
public class NoticeOneResponseDto {

    private final String title;
    private final String content;
    private final Long nor;
    private final Long noa;

    private final String dov; // yyyy-MM-dd
    private final String tov; // hh:mm:ss
    private final String dod; // yyyy-MM-dd

    public NoticeOneResponseDto() {
        this.title = null;
        this.content = null;
        this.nor = null;
        this.noa = null;
        this.dov = null;
        this.tov = null;
        this.dod = null;
    }
}
