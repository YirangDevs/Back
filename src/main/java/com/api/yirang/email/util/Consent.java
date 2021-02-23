package com.api.yirang.email.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@Getter
public enum Consent {

    CONSENT_YES("YES", true),
    CONSENT_NO("NO", false);

    private final String stringValue;
    private final Boolean booleanValue;
    private static final Map<String, Consent> nameMap;

    Consent(String stringValue, Boolean booleanValue) {
        this.stringValue = stringValue;
        this.booleanValue = booleanValue;
    }

    static{
        nameMap = new HashMap<>();
        nameMap.put("YES", CONSENT_YES);
        nameMap.put("네", CONSENT_YES);
        nameMap.put("yes", CONSENT_YES);
        nameMap.put("1", CONSENT_YES);
        nameMap.put("NO", CONSENT_NO);
        nameMap.put("아니오", CONSENT_NO);
        nameMap.put("no", CONSENT_NO);
        nameMap.put("0", CONSENT_NO);
    }

    @Override
    public String toString() {return stringValue;}

    public Boolean isAccept() {return booleanValue;}

    @JsonCreator
    public static Consent deserialize(String value){
        Consent output = nameMap.getOrDefault(value, null);
        if (output == null){
            throw new ConstraintViolationException(value + " should be one of YES or NO", null);
        }
        return output;
    }
    @JsonValue
    public String serialize() {return this.toString();}
}
