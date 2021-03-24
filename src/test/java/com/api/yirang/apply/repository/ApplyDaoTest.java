package com.api.yirang.apply.repository;


import com.api.yirang.apply.domain.model.Apply;
import com.api.yirang.apply.repository.persistence.maria.ApplyDao;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.notices.repository.persistence.maria.ActivityDao;
import com.api.yirang.seniors.support.custom.ServiceType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplyDaoTest {

    @Autowired
    private ApplyDao applyDao;

    @Autowired
    private ActivityDao activityDao;

    @Test
    public void 서비스_타입과_Acitivty로_조희하기(){
        Activity activity = activityDao.findById(545L).orElse(null);
        Collection<Apply> applies = applyDao.findAppliesByActivityAndServiceTypeOrderByDtoa(activity, ServiceType.SERVICE_WORK);
        System.out.println("Applies:" + applies);
    }
}
