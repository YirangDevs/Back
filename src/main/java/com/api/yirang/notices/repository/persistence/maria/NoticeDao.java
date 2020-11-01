package com.api.yirang.notices.repository.persistence.maria;

import com.api.yirang.auth.domain.user.model.Admin;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.notices.domain.notice.model.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface NoticeDao extends JpaRepository<Notice, Long> {

    @Override
    Optional<Notice> findById(Long noticeId);

    @Modifying
    @Transactional
    @Override
    void delete(Notice notice);

    @Transactional
    @Query("SELECT A " +
           "FROM Notice N " +
           "INNER JOIN Activity A " +
           "ON N.activity = A " +
           "WHERE N.noticeId =:noticeId")
    Optional<Activity> findActivityByNoticeId(Long noticeId);

    @Modifying
    @Transactional
    @Query("UPDATE Notice N " +
           "SET N.title =:newTitle, N.admin =:admin " +
           "WHERE N.noticeId =:noticeId")
    void updateWithTitleAndAdmin(Long noticeId, String newTitle, Admin admin);

    Optional<Notice> findNoticeByTitle(String title);

    boolean existsByTitle(String title);

    @Modifying
    @Transactional
    @Query("DELETE FROM Notice N")
    @Override
    void deleteAll();
}
