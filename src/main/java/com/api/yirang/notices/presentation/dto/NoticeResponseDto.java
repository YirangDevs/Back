package com.api.yirang.notices.presentation.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@AllArgsConstructor
public class NoticeResponseDto {

    private final Long id;
    private final String title;
    private final Long nor;

    private final String dov;
    private final String tov;

    public NoticeResponseDto() {
        this.id = null;
        this.title = null;
        this.nor = null;
        this.dov = null;
        this.tov = null;
    }
}
