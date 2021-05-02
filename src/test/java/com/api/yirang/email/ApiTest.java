package com.api.yirang.email;

import com.api.yirang.email.dto.AddressDto;
import com.api.yirang.email.repository.APIrepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.apache.http.client.HttpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ApiTest {

    @Autowired
    HttpClient httpClient;

    @Autowired
    APIrepository apIrepository;

    private static final String URL = "https://www.juso.go.kr/addrlink/addrLinkApi.do";
    private static final String KEY = "U01TX0FVVEgyMDIxMDUwMzAwMjI1NjExMTEyMDA=";
    private static final String KEYWORD = "성남대로 295";

    @Test
    public void 통계청_테스트() throws JsonProcessingException {

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();

        URI uri = UriComponentsBuilder.fromUriString(URL)
                                      .queryParam("currentPage", 1)
                                      .queryParam("countPerPage", 1)
                                      .queryParam("keyword", KEYWORD)
                                      .queryParam("resultType", "json")
                                      .queryParam("confmKey", KEY)
                                      .build().toUri();

        String responseText = restTemplate.getForObject(uri, String.class);

        System.out.println("response: " + responseText);

        ObjectReader reader = mapper.readerFor(AddressDto.class).withRootName("results");

        AddressDto addressDto = reader.readValue(responseText);

        System.out.println("지번: " + addressDto.getAddresses().get(0).get("jibunAddr"));
    }

    @Test
    public void API_레포지토리_테스트(){

        String jibun = apIrepository.getJibunFromAPI("성남대로 295");
        System.out.println("지번: " + jibun);
    }
}
