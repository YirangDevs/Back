package com.api.yirang.auth.repository.api;

import lombok.RequiredArgsConstructor;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@PropertySource("classpath:properties/application-kakao.properties")
public class KakaoInfoAPI {

    @Qualifier("YirangHttpClient")
    private final HttpClient yirangHttpClient;

}
