package com.api.yirang.common.service;


import com.api.yirang.common.domain.region.converter.RegionConverter;
import com.api.yirang.common.domain.region.exception.AlreadyExistedRegion;
import com.api.yirang.common.domain.region.exception.RegionNullException;
import com.api.yirang.common.domain.region.model.Region;
import com.api.yirang.common.presentation.dto.RegionRequestDto;
import com.api.yirang.common.repository.persistence.maria.RegionDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegionService {

    // DI Dao
    private final RegionDao regionDao;


    @Transactional
    public Region findRegionByRegionName(String regionName) {
        return regionDao.findRegionByRegionName(regionName).orElseThrow(RegionNullException::new);
    }

    public boolean isExistedByRegionName(String regionName){
        return regionDao.existsByRegionName(regionName);
    }

    @Transactional
    public void save(RegionRequestDto regionRequestDto){
        Region region = RegionConverter.fromRegionRequestDtoToRegion(regionRequestDto);
        if (isExistedByRegionName(region.getRegionName())){
            throw new AlreadyExistedRegion();
        }
        regionDao.save(region);
    }

    @Transactional
    public void deleteByRegionName(String regionName) {
        // 없으면 지우지 못함
        Region region = findRegionByRegionName(regionName);
        regionDao.deleteByRegionName(regionName);
    }
}
