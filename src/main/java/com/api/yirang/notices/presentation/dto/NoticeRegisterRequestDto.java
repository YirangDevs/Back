package com.api.yirang.notices.presentation.dto;


import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class NoticeRegisterRequestDto {

    private final String title;

    private final String content;

    private final Long nor; // number of request

    private final String dov; // date of Volunteer

    private final String tov; // time of Volunteer

    private final String dod; // deadline of volunteer

    private final String address;

    public NoticeRegisterRequestDto(String title, String content, Long nor, String dov,
                                    String tov, String dod, String address) {
        this.title = null;
        this.content = null;
        this.nor = null;
        this.dov = null;
        this.tov = null;
        this.dod = null;
        this.address = null;
    }
}
