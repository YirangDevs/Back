package com.api.yirang.common.service;


import com.api.yirang.auth.application.basicService.AdminService;
import com.api.yirang.auth.domain.user.model.Admin;
import com.api.yirang.common.domain.region.exception.AlreadyExistedDistribution;
import com.api.yirang.common.domain.region.exception.DistributionRegionNullException;
import com.api.yirang.common.domain.region.model.DistributionRegion;
import com.api.yirang.common.domain.region.model.Region;
import com.api.yirang.common.repository.persistence.maria.DistributionRegionDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class DistributionRegionService {

    // DI DAO
    private final DistributionRegionDao distributionRegionDao;

    // DI Other Service
    private final RegionService regionService;
    private final AdminService adminService;


    @Transactional
    public void save(Long userId, String regionName){

        // admin 찾기
        Admin admin = adminService.findAdminByUserId(userId);

        // region 찾기
        Region region = regionService.findRegionByRegionName(regionName);
        
        // 이미 배정이 된 경우, Exception을 줌
        if (existDistributionByAdminAndRegion(admin, region)){
            throw new AlreadyExistedDistribution();
        }

        // 저장하기
        distributionRegionDao.save(DistributionRegion.builder()
                                                     .admin(admin)
                                                     .region(region)
                                                     .build());
    }

    // Exist
    @Transactional
    public boolean existDistributionByAdminAndRegion(Admin admin, Region region){
        return distributionRegionDao.existsByAdminAndRegion(admin, region);
    }

    // 찾기
    @Transactional
    public DistributionRegion findDistributionByUserIdAndRegionName(Long userId, String regionName){
        return distributionRegionDao.findDistributionRegionByUserIdAndRegionName(userId, regionName)
                                    .orElseThrow(DistributionRegionNullException::new);
    }

    @Transactional
    public Collection<DistributionRegion> findDistributionsByUserId(Long userId){
        // Admin 찾기
        Admin admin = adminService.findAdminByUserId(userId);

        // Distribution 찾기
        Collection<DistributionRegion> distributionRegions =
                distributionRegionDao.findDistributionRegionsByUserId(userId);

        if (distributionRegions.size() == 0){
            throw new DistributionRegionNullException();
        }
        return distributionRegions;
    }

    @Transactional
    public Collection<DistributionRegion> findDistributionsByRegionName(String regionName){
        // Region 찾기
        Region region = regionService.findRegionByRegionName(regionName);

        // Distribution 찾기
        Collection<DistributionRegion> distributionRegions =
                distributionRegionDao.findDistributionRegionsByRegionName(regionName);

        if (distributionRegions.size() == 0){
            throw new DistributionRegionNullException();
        }
        return distributionRegions;
    }

    // 지우기
    @Transactional
    public void deleteByDistributionId(Long distributionId){
        DistributionRegion distributionRegion = distributionRegionDao.findById(distributionId)
                                                                     .orElseThrow(DistributionRegionNullException::new);
        distributionRegionDao.deleteDistributionRegionByDistributionId(distributionId);
    }

    @Transactional
    public void deleteByUserIdAndRegionName(Long userId, String regionName){
        DistributionRegion distributionRegion = findDistributionByUserIdAndRegionName(userId, regionName);
        deleteByDistributionId(distributionRegion.getDistributionId());
    }

    @Transactional
    public void deleteAllByUserId(Long userId){
        Admin admin = adminService.findAdminByUserId(userId);
        distributionRegionDao.deleteAllByUserId(admin);
    }
    @Transactional
    public void deleteAllByRegionName(String regionName){
        Region region = regionService.findRegionByRegionName(regionName);
        distributionRegionDao.deleteAllByRegionName(region);
    }
    @Transactional
    public void deleteAll(){
        distributionRegionDao.deleteAll();
    }

}
