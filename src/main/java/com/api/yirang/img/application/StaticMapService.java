package com.api.yirang.img.application;

import com.api.yirang.img.component.S3Uploader;
import com.api.yirang.img.model.GeoCode;
import com.api.yirang.img.model.mongo.MapImg;
import com.api.yirang.img.repository.API.NaverStaticMapAPI;
import com.api.yirang.img.repository.mongo.MapImgRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@PropertySource("classpath:properties/application-kakao.properties")
public class StaticMapService {

    private final S3Uploader s3Uploader;

    private final MapImgRepository mapImgRepository;

    private final NaverStaticMapAPI naverStaticMapAPI;

    private final String DEFAULT_MAP_IMG_URL;

    public StaticMapService(S3Uploader s3Uploader,
                            MapImgRepository mapImgRepository,
                            NaverStaticMapAPI naverStaticMapAPI,
                            @Value("${naver.api.default.map.img.url}") String DEFAULT_MAP_IMG_URL) {
        this.s3Uploader = s3Uploader;
        this.mapImgRepository = mapImgRepository;
        this.naverStaticMapAPI = naverStaticMapAPI;
        this.DEFAULT_MAP_IMG_URL = DEFAULT_MAP_IMG_URL;
    }

    public String findOrPullStaticMap(String mapName){

        // 0. 이미 같은 맵 이름으로 저장된 이미지가 있다면, 그걸 준다
        MapImg mapImg = mapImgRepository.findMapImgByMapName(mapName).orElse(null);
        if (mapImg != null){
            return mapImg.getMapImgUrl();
        }
        // 1. 만약 없다면, 아래의 과정을 수행한다.
        //  1-1. Naver API로 Static Map 이미지 얻기
        MultipartFile file = naverStaticMapAPI.getStaticMapImg(mapName);

        // 1-2 Xcode, yCode 얻기
        GeoCode geoCode = naverStaticMapAPI.getGeoCode(mapName);

        if (file == null){
            return DEFAULT_MAP_IMG_URL;
        }
        //  1-2. s3Uploader로 이미지를 업로드하고, 이미지 URL 받아오기
        String mapImgUrl = s3Uploader.uploadMap(file);
        //  1-3. mapImg Repository로 저장하기
        mapImgRepository.save(
                                MapImg.builder()
                                      .mapName(mapName)
                                      .xCode(geoCode.getXCode()).yCode(geoCode.getYCode())
                                      .mapImgUrl(mapImgUrl)
                                .build()
        );
        // 2. 만약 위 과정에서도 찾을 수 없다면, ~~을 찾을 수 없습니다 라는 이미지가 뜹니다.
        return mapImgRepository.findMapImgByMapName(mapName)
                               .orElse(MapImg.builder().mapName(null).mapImgUrl(DEFAULT_MAP_IMG_URL).build())
                               .getMapImgUrl();
    }

}
