package com.api.yirang.common.support.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@Getter
public enum Region {
    CENTRAL_DISTRICT("중구", "대구의 중구", "CENTRAL"),
    EAST_DISTRICT("동구", "대구의 동구", "EAST"),
    WEST_DISTRICT("서구", "대구의 서구", "WEST"),
    SOUTH_DISTRICT("남구", "대구의 남구", "SOUTH"),
    NORTH_DISTRICT("북구", "대구의 북구", "NORTH"),
    SOOSEONG_DISTRICT("수성구", "대구의 수성구", "SOOSEONG"),
    DALSEO_DISTRICT("달서구", "대구의 달서구", "DALSEO"),
    DALSEONGGUN_DISTRICT("달성군", "대구의 달성군", "DALSEONGGUN");

    private final String regionName;
    private final String description;
    private final String englishName;
    private static final Map<String, Region> nameMap;

    Region(String regionName, String description, String englishName) {
        this.regionName = regionName;
        this.description = description;
        this.englishName = englishName;
    }

    static{
        nameMap = new HashMap<>();
        nameMap.put("중구", CENTRAL_DISTRICT);
        nameMap.put("CENTRAL", CENTRAL_DISTRICT);
        nameMap.put("동구", EAST_DISTRICT);
        nameMap.put("EAST", EAST_DISTRICT);
        nameMap.put("서구", WEST_DISTRICT);
        nameMap.put("WEST", WEST_DISTRICT);
        nameMap.put("남구", SOUTH_DISTRICT);
        nameMap.put("SOUTH", SOUTH_DISTRICT);
        nameMap.put("북구", NORTH_DISTRICT);
        nameMap.put("NORTH", NORTH_DISTRICT);
        nameMap.put("수성구", SOOSEONG_DISTRICT);
        nameMap.put("SOOSEONG", SOOSEONG_DISTRICT);
        nameMap.put("달서구", DALSEO_DISTRICT);
        nameMap.put("DALSEO", DALSEO_DISTRICT);
        nameMap.put("달성군", DALSEONGGUN_DISTRICT);
        nameMap.put("DALSEONGGUN", DALSEONGGUN_DISTRICT);
    }

    @Override
    public String toString() {
        return regionName;
    }

    @JsonCreator
    public static Region deserialize(String value){
        Region output = nameMap.getOrDefault(value, null);
        if (output == null){
            throw new ConstraintViolationException(value + " should be one of 중구, 동구, 서구, 남구, " +
                                                   "북구, 수성구, 달서구, 달성군", null);
        }
        return output;
    }
    @JsonValue
    public String serialize() {return this.toString();}

}
