package com.api.yirang.img.util;

import com.api.yirang.email.util.Consent;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@Getter
public enum ImgType {

    IMG_TYPE_KAKAO("KAKAO"),
    IMG_TYPE_CUSTOM("CUSTOM");

    private final String option;
    private static final Map<String, ImgType> nameMap;

    ImgType(String option) {
        this.option = option;
    }

    static{
        nameMap = new HashMap<>();
        nameMap.put("KAKAO", IMG_TYPE_KAKAO);
        nameMap.put("카카오", IMG_TYPE_KAKAO);
        nameMap.put("CUSTOM", IMG_TYPE_CUSTOM);
        nameMap.put("사용자 정의", IMG_TYPE_CUSTOM);
    }

    @Override
    public String toString() {return option;}

    @JsonCreator
    public static ImgType deserialize(String value){
        ImgType output = nameMap.getOrDefault(value, null);
        if (output == null){
            throw new ConstraintViolationException(value + " should be one of [KAKAO, 카카오, CUSTOM, 사용자_정의]", null);
        }
        return output;
    }
    @JsonValue
    public String serialize() {return this.toString();}
}
