package com.api.yirang.apply.application;

import com.api.yirang.apply.domain.model.Apply;
import com.api.yirang.apply.repository.persistence.maria.ApplyDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by JeongminYoo on 2020/12/30
 * Work with Yirang
 * Email: likemin0142@gmail.com
 * Blog: http://Beewoom.github.io
 * Github: http://github.com/Biewoom
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ApplyBasicService {

    // DI field
    private final ApplyDao applyDao;

    public void save(Apply apply) {
        applyDao.save(apply);
    }
}
