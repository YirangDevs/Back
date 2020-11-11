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
    SEX_FEMALE("FEMALE", "Is a woman"),
    SEX_MALE("MALE", "Is a man"),
    SEX_UNKNOWN("UNKNOWN", "Is a man or Is a woman");

    private final String gender;
    private final String description;
    private static final Map<String, Sex> namesMap;

    Sex(String gender, String description) {
        this.gender = gender;
        this.description = description;
    }

    static {
        namesMap = new HashMap<>(3);
        namesMap.put("female", SEX_FEMALE);
        namesMap.put("male", SEX_MALE);
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
