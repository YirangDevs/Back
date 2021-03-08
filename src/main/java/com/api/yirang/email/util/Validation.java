package com.api.yirang.email.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

public enum Validation {

    VALIDATION_YES("YES", true),
    VALIDATION_NO("NO", false);

    private final String stringValue;
    private final Boolean booleanValue;
    private static final Map<String, Validation> nameMap;

    Validation(String stringValue, Boolean booleanValue) {
        this.stringValue = stringValue;
        this.booleanValue = booleanValue;
    }

    static{
        nameMap = new HashMap<>();
        nameMap.put("YES", VALIDATION_YES);
        nameMap.put("네", VALIDATION_YES);
        nameMap.put("yes", VALIDATION_YES);
        nameMap.put("1", VALIDATION_YES);
        nameMap.put("NO", VALIDATION_NO);
        nameMap.put("아니오", VALIDATION_NO);
        nameMap.put("no", VALIDATION_NO);
        nameMap.put("0", VALIDATION_NO);
    }

    @Override
    public String toString() {return stringValue;}

    public Boolean isValid() {return booleanValue;}

    @JsonCreator
    public static Validation deserialize(String value){
        Validation output = nameMap.getOrDefault(value, null);
        if (output == null){
            throw new ConstraintViolationException(value + " should be one of YES or NO", null);
        }
        return output;
    }
    @JsonValue
    public String serialize() {return this.toString();}
}
