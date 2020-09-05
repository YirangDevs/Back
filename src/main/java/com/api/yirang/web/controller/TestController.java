package com.api.yirang.web.controller;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class TestController {

    @CrossOrigin
    @PostMapping(value = "/auth/test", consumes = "application/json")
    public KakaoResponse authTest(@RequestBody RequestDto requestDto) throws IOException{

        System.out.println("requestDto2: " + requestDto);

        HttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost request = new HttpPost("https://kapi.kakao.com/v2/user/me");
        request.setHeader("Authorization", "Bearer " + requestDto.getAccess_token());

        System.out.println("requeset's Authorizaiton: "+ request.getHeaders("Authorization")[0]);
        HttpResponse response = httpClient.execute(request);
        String responseString = EntityUtils.toString(response.getEntity());
        System.out.println("code: " +response.getStatusLine().getStatusCode());
        System.out.println("response content: " + responseString);
        return new Gson().fromJson(responseString, KakaoResponse.class);
    }

}
