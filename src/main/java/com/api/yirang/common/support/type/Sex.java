package com.api.yirang.common.support.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintDeclarationException;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static org.springframework.data.util.Pair.toMap;

@Getter
public enum Sex {
    SEX_FEMAIL("FEMAIL", "Is a woman"),
    SEX_MAIL("MAIL", "Is a man"),
    SEX_UNKNOWN("UNKNOWN", "Is a man or Is a woman");

    private final String gender;
    private final String descrption;
    private static final Map<String, Sex> namesMap;

    Sex(String gender, String descrption) {
        this.gender = gender;
        this.descrption = descrption;
    }

    static {
        namesMap = new HashMap<>(3);
        namesMap.put("female", SEX_FEMAIL);
        namesMap.put("male", SEX_MAIL);
        namesMap.put("unknown", SEX_UNKNOWN);
    }

    @Override
    public String toString() {return gender;}

    @JsonCreator
    public static Sex deserialize(String value){
        Sex output = namesMap.getOrDefault(value.toLowerCase(), null);
        if (output == null){
            throw new ConstraintViolationException(value + " should be one of `male`, `female`, `unknown`", null);
        }
        return output;
    }

    @JsonValue
    public String serialize(){
        return this.toString();
    }

}
