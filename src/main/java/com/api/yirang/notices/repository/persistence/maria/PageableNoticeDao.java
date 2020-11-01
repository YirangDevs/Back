package com.api.yirang.notices.repository.persistence.maria;

import com.api.yirang.notices.domain.notice.model.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageableNoticeDao extends PagingAndSortingRepository<Notice, Long> {

    Page<Notice> findAll(Pageable pageable);
}
