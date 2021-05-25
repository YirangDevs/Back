package com.api.yirang.matching.application;

import com.api.yirang.img.model.GeoCode;
import com.api.yirang.img.repository.API.NaverStaticMapAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeoCodeService {

    private final NaverStaticMapAPI naverStaticMapAPI;

    public GeoCode getGeoCode(String mapName){
        return naverStaticMapAPI.getGeoCode(mapName);
    }
}
