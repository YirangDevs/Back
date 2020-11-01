package com.api.yirang.notices.application.basicService;


import com.api.yirang.auth.domain.user.model.Admin;
import com.api.yirang.notices.domain.activity.exception.ActivityNullException;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.notices.domain.notice.exception.AlreadyExistedNoticeException;
import com.api.yirang.notices.domain.notice.exception.NoticeNullException;
import com.api.yirang.notices.domain.notice.model.Notice;
import com.api.yirang.notices.repository.persistence.maria.NoticeDao;
import com.api.yirang.notices.repository.persistence.maria.PageableNoticeDao;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeService {

    // DI Daos
    private final NoticeDao noticeDao;
    private final PageableNoticeDao pageableNoticeDao;

    // DI Services
    private final ActivityService activityService;

    // Methods
    public Long save(Notice notice) {
        if (isExistedNoticeByNoticeTitle(notice.getTitle())){
            throw new AlreadyExistedNoticeException();
        }
        Notice returnedNotice = noticeDao.save(notice);
        return returnedNotice.getNoticeId();
    }

    public boolean isExistedNoticeByNoticeId(Long noticeId){
        return noticeDao.existsById(noticeId);
    }
    public boolean isExistedNoticeByNoticeTitle(String title){
        return noticeDao.existsByTitle(title);
    }

    public Notice findByNoticeId(Long noticeId) {
        return noticeDao.findById(noticeId).orElseThrow(NoticeNullException::new);
    }

    public Notice findByNoticeTitle(String title){
        return noticeDao.findNoticeByTitle(title).orElseThrow(NoticeNullException::new);
    }


    public Collection<Notice> findAllWithPage(Pageable pageWithSixElements) {
        Page<Notice> noticePage = pageableNoticeDao.findAll(pageWithSixElements);
        Collection<Notice> notices = noticePage.toList();

        if (notices.size() == 0){
            throw new NoticeNullException();
        }

        return notices;
    }

    public Long countNumsOfNotices() {
        return noticeDao.count();
    }

    public Activity findActivityNoticeId(Long noticeId){
        return noticeDao.findActivityByNoticeId(noticeId).orElseThrow(ActivityNullException::new);
    }

    public void update(Long noticeId, String newTitle, Admin admin) {
        // 일단 없으면 Erorr
        Notice notice = findByNoticeId(noticeId);
        noticeDao.updateWithTitleAndAdmin(noticeId, newTitle, admin);
    }

    public void deleteNoticeByNoticeId(Long noticeId) {
        // 일단 없으면 Error
        Notice notice = findByNoticeId(noticeId);
        noticeDao.delete(notice);
    }
    public void deleteAll(){
        noticeDao.deleteAll();
    }
}
