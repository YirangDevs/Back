package com.api.yirang.email.repository;

import com.api.yirang.common.exceptions.OtherServerException;
import com.api.yirang.email.dto.AddressDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Repository
public class APIrepository {

    private final RestTemplate yirangRestTempalte;
    private final ObjectMapper yirangObjectMapper;

    private static final String JIBUN_URL = "https://www.juso.go.kr/addrlink/addrLinkApi.do";
    private static final String JIBUN_KEY = "U01TX0FVVEgyMDIxMDUwMzAwMjI1NjExMTEyMDA=";

    public APIrepository(
            @Qualifier("YirangRestTemplate") RestTemplate yirangRestTempalte,
            @Qualifier("YirangObjectMapper") ObjectMapper yirangObjectMapper
    ) {
        this.yirangRestTempalte = yirangRestTempalte;
        this.yirangObjectMapper = yirangObjectMapper;
    }

    public String getJibunFromAPI(String roadAddress){
        try{

            URI uri = UriComponentsBuilder.fromUriString(JIBUN_URL)
                                          .queryParam("currentPage", 1)
                                          .queryParam("countPerPage", 1)
                                          .queryParam("keyword", roadAddress)
                                          .queryParam("resultType", "json")
                                          .queryParam("confmKey", JIBUN_KEY)
                                          .build().toUri();

            String responseText = yirangRestTempalte.getForObject(uri, String.class);
            System.out.println("response: " + responseText);

            ObjectReader reader = yirangObjectMapper.readerFor(AddressDto.class).withRootName("results");

            AddressDto addressDto = reader.readValue(responseText);

            if (addressDto.getAddresses().isEmpty()){
                return "없음";
            }

            return addressDto.getAddresses().get(0).get("jibunAddr").toString();
        }
        catch (Exception e){
            throw new OtherServerException("011", "There is a problem From Connection between RoadAddress API");
        }
    }

}
