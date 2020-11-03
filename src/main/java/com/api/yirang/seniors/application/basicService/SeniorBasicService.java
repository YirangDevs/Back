package com.api.yirang.seniors.application.basicService;


import com.api.yirang.common.domain.region.model.Region;
import com.api.yirang.seniors.domain.senior.exception.SeniorNullException;
import com.api.yirang.seniors.domain.senior.model.Senior;
import com.api.yirang.seniors.repository.persistence.maria.SeniorDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeniorBasicService {

    private final SeniorDao seniorDao;



    // Count
    public Long countAll() {
        return seniorDao.count();
    }

    public Long countSeniorsByRegion(Region region) {
        return seniorDao.countSeniorsByRegion(region);
    }

    // exist
    public boolean isExistByPhone(String phone) {
        return seniorDao.existsSeniorByPhone(phone);
    }

    public void save(Senior senior) {
        seniorDao.save(senior);
    }

    // Find
    public Senior findSeniorByPhone(String phone) {
        return seniorDao.findSeniorByPhone(phone).orElseThrow(SeniorNullException::new);
    }
}
