package com.api.yirang.notices.repository.persistence.maria;

import com.api.yirang.notices.domain.activity.model.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageableActivityDao extends PagingAndSortingRepository<Activity, Long> {

    Page<Activity> findAll(Pageable pageable);
}


