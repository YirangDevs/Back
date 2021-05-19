package com.api.yirang.img.repository.API;

import com.api.yirang.img.dto.GeocodeDto;
import com.api.yirang.img.model.GeoCode;
import com.api.yirang.img.util.InvalidNaverAPIException;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Repository
@PropertySource("classpath:properties/application-naver.properties")
public class NaverStaticMapAPI {

    private final HttpClient yirangHttpClient;

    private final String naverStaticMapApiUrl;
    private final String naverGeocodeApiUrl;

    private final String naverClientId;
    private final String naverClientSecret;

    private final String naverMapLevel;
    private final String naverMapWidth;
    private final String naverMapHeight;
    private final String naverMapColorcode;
    private final String naverMapScale;

    public NaverStaticMapAPI(@Qualifier("YirangHttpClient") HttpClient yirangHttpClient,
                             @Value("${naver.api.naver.static.map.url}") String naverStaticMapApiUrl,
                             @Value("${naver.api.naver.geocode.url}") String naverGeocodeApiUrl,
                             @Value("${naver.api.naver.client.id}") String naverClientId,
                             @Value("${naver.api.naver.client.secret}") String naverClientSecret,
                             @Value("${naver.api.map.level}") String naverMapLevel,
                             @Value("${naver.api.map.width}") String naverMapWidth,
                             @Value("${naver.api.map.height}") String naverMapHeight,
                             @Value("${naver.api.map.colorcode}") String naverMapColorcode,
                             @Value("${naver.api.map.scale}") String naverMapScale
    ) {
        this.yirangHttpClient = yirangHttpClient;
        this.naverStaticMapApiUrl = naverStaticMapApiUrl;
        this.naverGeocodeApiUrl = naverGeocodeApiUrl;
        this.naverClientId = naverClientId;
        this.naverClientSecret = naverClientSecret;
        this.naverMapLevel = naverMapLevel;
        this.naverMapWidth = naverMapWidth;
        this.naverMapHeight = naverMapHeight;
        this.naverMapColorcode = naverMapColorcode;

        this.naverMapScale = naverMapScale;
    }

    private String makeStaticMapUrl(String xCode, String yCode) throws UnsupportedEncodingException {
        return
                 "w=" + naverMapWidth
               + "&&h=" + naverMapHeight
               + "&&center=" + xCode + "," + yCode
               + "&&level=" + naverMapLevel
               + "&&scale=" + naverMapScale
               + "&&markers=" + URLEncoder.encode("type:d|size:small|color:"+ naverMapColorcode +"|pos:" + xCode + " " + yCode, "UTF-8");
    }


    public MultipartFile getStaticMapImg(String mapName){
        try{
            GeoCode geoCode = getGeoCode(mapName);

            if (geoCode == null){
                return null;
            }

            String URL = naverStaticMapApiUrl + "?" + makeStaticMapUrl(geoCode.getXCode(), geoCode.getYCode());

            HttpGet httpGet = new HttpGet(URL);

            httpGet.setHeader("X-NCP-APIGW-API-KEY-ID", naverClientId);
            httpGet.setHeader("X-NCP-APIGW-API-KEY", naverClientSecret);

            System.out.println("[NaverStaticMapAPI] Naver Static MAP IMG Request: " + httpGet);

            HttpResponse response = yirangHttpClient.execute(httpGet);
            byte[] output = EntityUtils.toByteArray(response.getEntity());
            InputStream inputStream = new ByteArrayInputStream(output);
            return new MockMultipartFile("new.png", "Original", ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream);

        } catch (IOException e) {
            e.printStackTrace();
            throw new InvalidNaverAPIException();
        }

    }

    public GeoCode getGeoCode(String mapName){


        try {
            String URL = naverGeocodeApiUrl + "?query=" + URLEncoder.encode(mapName, "UTF-8");

            HttpGet httpGet = new HttpGet(URL);
            httpGet.setHeader("X-NCP-APIGW-API-KEY-ID", naverClientId);
            httpGet.setHeader("X-NCP-APIGW-API-KEY", naverClientSecret);


            System.out.println("[NaverStaticMapAPI] GeoCode Request: " + httpGet);

            HttpResponse response = yirangHttpClient.execute(httpGet);
            String responseString = EntityUtils.toString(response.getEntity());

            System.out.println("[NaverStaticMapAPI] GeoCode Response: " + responseString);

            GeocodeDto geocodeDto = new Gson().fromJson(responseString, GeocodeDto.class);

            System.out.println("ADDRESSES: " + geocodeDto.getAddresses());

            if (geocodeDto.getAddresses().isEmpty()){
                return null;
            }

            String xCode = (String) geocodeDto.getAddresses().get(0).get("x");
            String yCode = (String) geocodeDto.getAddresses().get(0).get("y");

            return GeoCode.builder()
                          .xCode(xCode)
                          .yCode(yCode)
                          .build();

        } catch (IOException e) {
            e.printStackTrace();
            throw new InvalidNaverAPIException();
        }
    }


}
