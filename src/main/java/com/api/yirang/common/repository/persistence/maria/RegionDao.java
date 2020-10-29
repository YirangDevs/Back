package com.api.yirang.common.repository.persistence.maria;

import com.api.yirang.common.domain.region.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RegionDao extends JpaRepository<Region, Long> {

    Optional<Region> findRegionByRegionName(String regionName);

    boolean existsByRegionName(String regionName);

    @Modifying
    @Transactional
    @Query("DELETE FROM Region AS r WHERE r.regionName=:regionName")
    void deleteByRegionName(String regionName);
}
