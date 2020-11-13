package com.api.yirang.common.repository.persistence.maria;

import com.api.yirang.auth.domain.user.model.Admin;
import com.api.yirang.common.domain.region.model.DistributionRegion;
import com.api.yirang.common.domain.region.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;


@Repository
public interface DistributionRegionDao extends JpaRepository<DistributionRegion, Long> {


    @Modifying
    @Transactional
    @Query("DELETE FROM DistributionRegion D " +
           "WHERE D.distributionId = :distributionId")
    void deleteDistributionRegionByDistributionId(Long distributionId);

    @Transactional
    @Query("SELECT D.region " +
           "FROM DistributionRegion AS D " +
           "WHERE D.admin.adminNumber=:adminNum ")
    Set<Region> findRegionsByAdminNum(Long adminNum);


    @Transactional
    @Query("SELECT D " +
           "FROM DistributionRegion AS D " +
           "WHERE D.admin.user.userId = :userId AND D.region.regionName = :regionName")
    Optional<DistributionRegion> findDistributionRegionByUserIdAndRegionName(Long userId, String regionName);

    @Transactional
    @Query("SELECT D " +
           "FROM DistributionRegion AS D " +
           "WHERE D.admin.user.userId = :userId ")
    Set<DistributionRegion> findDistributionRegionsByUserId(Long userId);

    @Transactional
    @Query("SELECT D " +
           "FROM DistributionRegion AS D " +
           "WHERE D.region.regionName = :regionName")
    Set<DistributionRegion> findDistributionRegionsByRegionName(String regionName);

    @Transactional
    @Modifying
    @Query("DELETE " +
           "FROM DistributionRegion AS D " +
           "WHERE D.admin = :admin")
    void deleteAllByUserId(Admin admin);

    @Transactional
    @Modifying
    @Query("DELETE " +
           "FROM DistributionRegion AS D " +
           "WHERE D.region = :region")
    void deleteAllByRegionName(Region region);

    @Override
    @Transactional
    @Modifying
    @Query("DELETE FROM DistributionRegion D")
    void deleteAll();

    boolean existsByAdminAndRegion(Admin admin, Region region);
}
