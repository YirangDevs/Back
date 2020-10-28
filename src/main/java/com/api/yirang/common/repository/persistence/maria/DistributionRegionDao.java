package com.api.yirang.common.repository.persistence.maria;

import com.api.yirang.common.domain.region.model.DistributionRegion;
import com.api.yirang.common.domain.region.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;


@Repository
public interface DistributionRegionDao extends JpaRepository<DistributionRegion, Long> {

    @Transactional
    @Query("SELECT D.region " +
           "FROM DistributionRegion AS D " +
           "WHERE D.admin.adminNumber=:adminNum ")
    Collection<Region> findRegionsByAdminNum(Long adminNum);
}
