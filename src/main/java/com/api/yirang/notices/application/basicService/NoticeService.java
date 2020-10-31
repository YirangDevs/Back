package com.api.yirang.notices.application.basicService;


import com.api.yirang.notices.domain.notice.exception.NoticeNullException;
import com.api.yirang.notices.domain.notice.model.Notice;
import com.api.yirang.notices.presentation.dto.NoticeResponsesDto;
import com.api.yirang.notices.repository.persistence.maria.NoticeDao;
import com.api.yirang.notices.repository.persistence.maria.PageableNoticeDao;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class NoticeService {

    // DI Daos
    private final NoticeDao noticeDao;
    private final PageableNoticeDao pageableNoticeDao;

    public void save(Notice notice) {
        noticeDao.save(notice);
    }

    public Notice findByNoticeId(Long noticeId) {
        return noticeDao.findById(noticeId).orElseThrow(NoticeNullException::new);
    }

    public Collection<Notice> findAllWithPage(Pageable pageWithSixElements) {
        Page<Notice> noticePage = pageableNoticeDao.findAll(pageWithSixElements);
        Collection<Notice> notices = noticePage.toList();

        if (notices.size() == 0){
            throw new NoticeNullException();
        }

        return notices;
    }
}
