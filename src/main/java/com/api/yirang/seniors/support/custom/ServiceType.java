package com.api.yirang.seniors.support.custom;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@Getter
public enum ServiceType {

    SERVICE_WORK("WORK", "The Type Of Work That Need Lots Of Energy"),
    SERVICE_TALK("TALK", "The Type Of Work That Need little Energy"),
    SERVICE_BOTH("BOTH", "The Type of Work That indicating both of above types");

    private final String type;
    private final String description;
    private static final Map<String, ServiceType> namesMap;

    ServiceType(String type, String description) {
        this.type = type;
        this.description = description;
    }

    static {
        namesMap = new HashMap<>(3);
        namesMap.put("work", SERVICE_WORK);
        namesMap.put("talk", SERVICE_TALK);
        namesMap.put("both", SERVICE_BOTH);
    }

    @Override
    public String toString(){return type;}

    @JsonCreator
    public static ServiceType deserialize(String value){
        ServiceType output = namesMap.getOrDefault(value.toLowerCase(), null);
        if (output == null){
            throw new ConstraintViolationException(value + " should be `work` or `talk` or `both`", null);
        }
        return output;
    }

    @JsonValue
    public String serialize(){
        return this.toString();
    }
}
