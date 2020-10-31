package com.api.yirang.notices.presentation.dto;

import com.api.yirang.notices.domain.notice.model.Notice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Collection;

@Getter
@ToString
@AllArgsConstructor
@Builder
public class NoticeResponsesDto {

    private final Collection<NoticeResponseDto> notices;

    public NoticeResponsesDto() {
        this.notices = null;
    }
}
