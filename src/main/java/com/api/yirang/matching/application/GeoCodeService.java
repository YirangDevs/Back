package com.api.yirang.matching.application;

import com.api.yirang.img.model.GeoCode;
import com.api.yirang.img.model.mongo.MapImg;
import com.api.yirang.img.repository.API.NaverStaticMapAPI;
import com.api.yirang.img.repository.mongo.MapImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeoCodeService {

    private final NaverStaticMapAPI naverStaticMapAPI;
    private final MapImgRepository mapImgRepository;

    public GeoCode getGeoCode(String mapName){

        MapImg mapImg = mapImgRepository.findMapImgByMapName(mapName).orElse(null);
        if (mapImg != null){
            return GeoCode.builder()
                          .xCode(mapImg.getXCode()).yCode(mapImg.getYCode())
                          .build();
        }
        return naverStaticMapAPI.getGeoCode(mapName);
    }
}
