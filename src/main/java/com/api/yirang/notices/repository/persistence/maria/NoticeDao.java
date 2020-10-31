package com.api.yirang.notices.repository.persistence.maria;

import com.api.yirang.notices.domain.notice.model.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NoticeDao extends JpaRepository<Notice, Long> {

    @Override
    Optional<Notice> findById(Long aLong);
}
