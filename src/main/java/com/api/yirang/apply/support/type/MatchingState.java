package com.api.yirang.apply.support.type;

import com.api.yirang.common.support.type.Region;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by JeongminYoo on 2021/1/7
 * Work with Yirang
 * Email: likemin0142@gmail.com
 * Blog: http://Beewoom.github.io
 * Github: http://github.com/Biewoom
 */
@Getter
public enum MatchingState {
    MATCHING_READY("매칭대기", "매칭이 아직 되지않고 대기하는 상태"),
    MATCHING_SUCCESS("매칭성공", "매칭이 성공한 상태"),
    MATCHING_FAIL("매칭실패", "매칭이 실패한 상태");

    private final String state;
    private final String description;

    private static final Map<String, MatchingState> nameMap;

    MatchingState(String state, String description) {
        this.state = state;
        this.description = description;
    }

    static{
        nameMap = new HashMap<>();
        nameMap.put("매칭대기", MATCHING_READY);
        nameMap.put("매칭성공", MATCHING_READY);
        nameMap.put("매칭실패", MATCHING_READY);
    }

    @Override
    public String toString() {
        return state;
    }

    @JsonCreator
    public static MatchingState deserialize(String value){
        MatchingState output = nameMap.getOrDefault(value, null);
        if (output == null){
            throw new ConstraintViolationException(value + " should be one of 매칭대기, 매칭성공, 매칭실패", null);
        }
        return output;
    }

    @JsonValue
    public String serialize() {return this.toString();}
}
