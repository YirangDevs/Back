package com.api.yirang.web.controller;

import com.api.yirang.web.controller.VO.Kakao_account;
import com.api.yirang.web.controller.VO.Properties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoResponse {
    private long id;
    private Properties properties;
    private Kakao_account kakao_account;
    private String synched_at;
    private String connected_at;
}
