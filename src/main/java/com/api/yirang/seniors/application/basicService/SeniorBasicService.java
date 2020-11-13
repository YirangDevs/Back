package com.api.yirang.seniors.application.basicService;


import com.api.yirang.common.domain.region.model.Region;
import com.api.yirang.common.support.type.Sex;
import com.api.yirang.seniors.domain.senior.exception.SeniorNullException;
import com.api.yirang.seniors.domain.senior.model.Senior;
import com.api.yirang.seniors.repository.persistence.maria.SeniorDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Transactional
public class SeniorBasicService {

    private final SeniorDao seniorDao;


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

    public Collection<Senior> findSeniorsByRegion(Region region, Boolean nullable) {
        Collection<Senior> seniors = seniorDao.findSeniorsByRegion(region);
        if (seniors.size() == 0 && !nullable){
            throw new SeniorNullException();
        }
        return seniors;
    }
    // update

    public void updateSenior(Senior existedSenior, String name, Sex sex, String address, String phone, Region region) {
        seniorDao.updateSenior(existedSenior, name, sex, address, phone, region);
    }
}
