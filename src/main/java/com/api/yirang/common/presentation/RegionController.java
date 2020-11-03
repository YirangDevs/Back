package com.api.yirang.common.presentation;


import com.api.yirang.common.presentation.dto.RegionRequestDto;
import com.api.yirang.common.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/apis/region")
@RequiredArgsConstructor
public class RegionController {

    // DI service
    private final RegionService regionService;

    // Post /v1/apis/region에 지역 이름 기억 적으면
    // 그 Region 이 저장됨
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registerRegion(@RequestBody RegionRequestDto regionRequestDto){
        regionService.save(regionRequestDto);
    }
    // Delete /v1/apis/region?region_name={region_name}
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRegion(@RequestParam(name = "region_name") String regionName){
        regionService.deleteByRegionName(regionName);
    }
}
