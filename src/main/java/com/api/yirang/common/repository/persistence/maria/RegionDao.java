package com.api.yirang.common.repository.persistence.maria;

import com.api.yirang.common.domain.region.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegionDao extends JpaRepository<Region, Long> {

    Optional<Region> findRegionByRegionName(String regionName);
}
