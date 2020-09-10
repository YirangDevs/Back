package com.api.yirang.common.support.charset;

import java.util.Base64;

public class CharsetUtils {

    public static final String encodeBase64(String str){
        return Base64.getEncoder().encodeToString(str.getBytes());
    }

    public static final String decodeBase64(String encodedStr){
        return new String(Base64.getDecoder().decode(encodedStr));
    }
}
