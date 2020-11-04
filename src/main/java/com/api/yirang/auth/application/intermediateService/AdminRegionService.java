package com.api.yirang.auth.application.intermediateService;


import com.api.yirang.auth.application.basicService.AdminService;
import com.api.yirang.auth.domain.user.model.Admin;
import com.api.yirang.auth.presentation.dto.regionsResponseDto;
import com.api.yirang.common.domain.region.model.DistributionRegion;
import com.api.yirang.common.domain.region.model.Region;
import com.api.yirang.common.repository.persistence.maria.DistributionRegionDao;
import com.api.yirang.common.service.DistributionRegionService;
import com.api.yirang.common.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminRegionService {


    // DI Basic
    private final AdminService adminService;
    private final RegionService regionService;
    private final DistributionRegionService distributionRegionService;

    // Methods

    @Transactional
    public void delegateRegion(Long userId, String regionName){
        // distribution에 저장함
        distributionRegionService.save(userId, regionName);
    }

    @Transactional
    public void unDelegateRegion(Long userId, String regionName) {
        distributionRegionService.deleteByUserIdAndRegionName(userId, regionName);
    }

    @Transactional
    public Collection<String> getMyRegions(Long userId) {

        Collection<DistributionRegion> distributionRegions = distributionRegionService.findDistributionsByUserId(userId);
        return distributionRegions.stream().map(e -> e.getRegion().getRegionName()).collect(Collectors.toList());

    }
}
