package com.api.yirang.common.configure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class HttpClientConfig {

    @Bean(name="YirangHttpClient")
    public HttpClient makeHttpClient(){
        return HttpClientBuilder.create().build();
    }

    @Bean(name = "YirangRestTemplate")
    public RestTemplate makeRestTemplate() {return new RestTemplate();}

    @Bean(name = "YirangObjectMapper")
    public ObjectMapper makeObjectMapper() {return new ObjectMapper();}
}
