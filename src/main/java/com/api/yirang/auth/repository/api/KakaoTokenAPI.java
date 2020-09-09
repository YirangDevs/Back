package com.api.yirang.auth.repository.api;

import com.api.yirang.auth.domain.kakaoToken.dto.KakaoTokenInfoDto;
import com.api.yirang.auth.domain.kakaoToken.exceptions.AlreadyExpiredKakaoAccessTokenException;
import com.api.yirang.auth.domain.kakaoToken.exceptions.InvalidKakaoAccessTokenException;
import com.api.yirang.auth.domain.kakaoToken.exceptions.KakaoServerException;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
@RequiredArgsConstructor
@PropertySource("classpath:properties/application-kakao.properties")
public class KakaoTokenAPI {

    @Qualifier("YirangHttpClient")
    private final HttpClient yirangHttpClient;

    // Kakao Token info
    @Value("${kakao.api.token.access.url}")
    private String kakaoAccessTokenInfoUrl;
    @Value("${kakao.api.token.access.content_type}")
    private String kakaoAccessTokenInfoContentType;

    private KakaoTokenInfoDto connectAndGetKakaoTokenInfo(String kakaoAccessToken){

        try {
            HttpGet request = new HttpGet(kakaoAccessTokenInfoUrl);
            request.setHeader("Authorization", "Bearer " + kakaoAccessToken);

            // for debugging
            System.out.println("request's Authorization: " + request.getHeaders("Authorization")[0]);

            HttpResponse response = yirangHttpClient.execute(request);
            String responseString = EntityUtils.toString(response.getEntity());

            //for debugging
            System.out.println("Code: " + response.getStatusLine().getStatusCode());
            System.out.println("response content: " + responseString);

            // Kakao Reponse이 실패하면 KAT가 Invalid 한 것
            if (response.getStatusLine()
                        .getStatusCode() != 200) {
                throw new InvalidKakaoAccessTokenException();
            }
            return new Gson().fromJson(responseString, KakaoTokenInfoDto.class);
        }
        catch(IOException e){
            throw new KakaoServerException();
        }
    }

    public void isValidKakaoAccessToken(String kakaoAccessToken) {

        KakaoTokenInfoDto kakaoTokenInfo = connectAndGetKakaoTokenInfo(kakaoAccessToken);
        // Kakao Token을 뜯어봤더니, 남은 시간이 없다!
        if (kakaoTokenInfo.getExpiresIn() <= 0) {
            throw new AlreadyExpiredKakaoAccessTokenException();
        }
    }

    public Long getUserId(String kakaoAccessToken) {
        KakaoTokenInfoDto kakaoTokenInfo = connectAndGetKakaoTokenInfo(kakaoAccessToken);
        return kakaoTokenInfo.getId();
    }
}

