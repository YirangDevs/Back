package com.api.yirang.auth.application.intermediateService;


import com.api.yirang.auth.application.basicService.AdminService;
import com.api.yirang.auth.domain.user.model.Admin;
import com.api.yirang.common.domain.region.model.DistributionRegion;
import com.api.yirang.common.domain.region.model.Region;
import com.api.yirang.common.repository.persistence.maria.DistributionRegionDao;
import com.api.yirang.common.service.DistributionRegionService;
import com.api.yirang.common.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        // userId로 Admin을 찾고
        Admin foundAdmin = adminService.findAdminByUserId(userId);
        // regionName로 Region
        Region foundRegion = regionService.findRegionByRegionName(regionName);

        // Distribution 만들기
        DistributionRegion newDistributionRegion = DistributionRegion.builder()
                                                                     .admin(foundAdmin)
                                                                     .region(foundRegion)
                                                                     .build();
        // distribution에 저장함
        distributionRegionService.save(newDistributionRegion);

    }
}
