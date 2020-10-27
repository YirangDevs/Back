package com.api.yirang.common.repository.persistence.maria;

import com.api.yirang.common.domain.region.model.DistributionRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistributionRegionDao extends JpaRepository<DistributionRegion, Long> {

}
