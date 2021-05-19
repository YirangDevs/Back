package com.api.yirang.img;

import com.api.yirang.img.application.StaticMapService;
import com.api.yirang.img.model.GeoCode;
import com.api.yirang.img.repository.API.NaverStaticMapAPI;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class NaverAPITest {

    @Autowired
    NaverStaticMapAPI naverStaticMapAPI;

    @Autowired
    StaticMapService staticMapService;


    @Test
    public void GEOCODE_얻기(){
        GeoCode geoCode = naverStaticMapAPI.getGeoCode("불정로 6 그린팩토리");
        System.out.println("GEOCODE: " + geoCode);
    }

    @Test
    public void Static_MAP_API_얻기(){
        naverStaticMapAPI.getStaticMapImg("불정로 6 그린팩토리");
    }

    @Test
    public void Static_MAP_얻어서_저장하기(){
        String res = staticMapService.findOrPullStaticMap("이곡동 1300");
        System.out.println("RES: " + res);
    }

}
