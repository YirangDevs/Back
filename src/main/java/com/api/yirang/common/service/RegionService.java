package com.api.yirang.common.service;


import com.api.yirang.common.domain.region.converter.RegionConverter;
import com.api.yirang.common.domain.region.exception.AlreadyExistedRegion;
import com.api.yirang.common.domain.region.exception.RegionNullException;
import com.api.yirang.common.domain.region.model.Region;
import com.api.yirang.common.presentation.dto.RegionRequestDto;
import com.api.yirang.common.repository.persistence.maria.RegionDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegionService {

    // DI Dao
    private final RegionDao regionDao;


    public final Region findRegionByRegionName(String regionName) {
        return regionDao.findRegionByRegionName(regionName).orElseThrow(RegionNullException::new);
    }

    public final boolean isExistedByRegionName(String regionName){
        return regionDao.existsByRegionName(regionName);
    }

    public final void save(RegionRequestDto regionRequestDto){
        Region region = RegionConverter.fromRegionRequestDtoToRegion(regionRequestDto);
        if (isExistedByRegionName(region.getRegionName())){
            throw new AlreadyExistedRegion();
        }
        regionDao.save(region);
    }

    public void deleteByRegionName(String regionName) {
        // 없으면 지우지 못함
        Region region = findRegionByRegionName(regionName);
        regionDao.deleteByRegionName(regionName);
    }
}
