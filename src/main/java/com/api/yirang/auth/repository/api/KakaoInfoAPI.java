package com.api.yirang.auth.repository.api;

import com.api.yirang.auth.domain.kakaoToken.dto.KakaoUserInfoDto;
import com.api.yirang.auth.domain.kakaoToken.exceptions.InvalidKakaoAccessTokenException;
import com.api.yirang.auth.domain.kakaoToken.exceptions.KakaoServerException;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
@PropertySource("classpath:properties/application-kakao.properties")
public class KakaoInfoAPI {


    private final HttpClient yirangHttpClient;

    private final String kakaoUserInfoUrl;

    private final String kakaoUserInfoContentType;

    public KakaoInfoAPI(@Qualifier("YirangHttpClient") HttpClient yirangHttpClient,
                        @Value("${kakao.api.token.info.url}") String kakaoUserInfoUrl,
                        @Value("${kakao.api.token.info.content_type}") String kakaoUserInfoContentType) {
        this.yirangHttpClient = yirangHttpClient;
        this.kakaoUserInfoUrl = kakaoUserInfoUrl;
        this.kakaoUserInfoContentType = kakaoUserInfoContentType;
    }

    private String getListOfUserInfoKeys(){
        return "[" + " \"" + "properties.nickname" + "\""
               + ",\"" + "properties.profile_image" + "\""
               + ",\"" + "kakao_account.email" + "\""
               + ",\"" + "kakao_account.gender" + "\""
               + "]";
    }

    public KakaoUserInfoDto getUserInfo(String kakaoAccessToken) {
        // [TO-Do] get user information by using kakaoToken
        // xhttp로 보내야함
        try{
            String keys = getListOfUserInfoKeys();

            // debug
            System.out.println("keys: " + keys);

            List<NameValuePair> form = new ArrayList<>();
            form.add(new BasicNameValuePair("property_keys", keys) );
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form);

            HttpPost request = new HttpPost(kakaoUserInfoUrl);
            request.setEntity(entity);
            request.setHeader("Authorization", "Bearer " + kakaoAccessToken);
            request.setHeader("Content-Type", kakaoUserInfoContentType);

            HttpResponse response = yirangHttpClient.execute(request);
            String responseString = EntityUtils.toString(response.getEntity());

            // For debugging
            System.out.println("Code: " + response.getStatusLine().getStatusCode());
            System.out.println("response content: " + responseString);

            int status = response.getStatusLine().getStatusCode();
            if(status != 200){
                throw new InvalidKakaoAccessTokenException();
            }
            return new Gson().fromJson(responseString, KakaoUserInfoDto.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new KakaoServerException();
        }
    }


}
