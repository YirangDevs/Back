package com.api.yirang.common.service;


import com.api.yirang.common.domain.region.model.DistributionRegion;
import com.api.yirang.common.repository.persistence.maria.DistributionRegionDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DistributionRegionService {

    // DI DAO
    private final DistributionRegionDao distributionRegionDao;

    public void save(DistributionRegion distributionRegion){
        distributionRegionDao.save(distributionRegion);
    }
}
