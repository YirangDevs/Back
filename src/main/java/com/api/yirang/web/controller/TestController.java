package com.api.yirang.web.controller;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TestController {

    @CrossOrigin
    @PostMapping(value = "/auth/test", consumes = "application/json")
    public ResponseDto authTest(@RequestBody RequestDto requestDto) throws IOException, InterruptedException {

        System.out.println("requestDto: " + requestDto);

        HttpClient httpClient = HttpClient.newBuilder()
                                          .version(HttpClient.Version.HTTP_1_1)
                                          .build();

        String kakaoAccessToken = requestDto.getAccess_token();

        List myList = new ArrayList<String>();
        myList.add("properties.nickname");

        Map<Object, Object> data = new HashMap<>();
        data.put("property_keys", myList);

        HttpRequest request = HttpRequest.newBuilder()
                                         .uri(URI.create("https://kapi.kakao.com/v2/user/me"))
                                         .timeout(Duration.ofMinutes(1))
                                         .setHeader("Authorization", "Bearer " + kakaoAccessToken)
                                         .POST(buildFormDataFromMap(data))
                                         .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return new Gson().fromJson(response.body(), ResponseDto.class);
    }

    private HttpRequest.BodyPublisher buildFormDataFromMap(Map<Object, Object> data){
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry: data.entrySet()){
            if(builder.length() > 0){
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append((URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8)));
        }
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }
}
