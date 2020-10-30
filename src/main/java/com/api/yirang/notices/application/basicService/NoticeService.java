package com.api.yirang.notices.application.basicService;


import com.api.yirang.notices.repository.persistence.maria.NoticeDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeDao noticeDao;

}
