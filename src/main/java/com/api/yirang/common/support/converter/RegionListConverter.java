package com.api.yirang.common.support.attributeConverter;

import com.api.yirang.common.support.type.Region;
import com.api.yirang.common.support.type.Regions;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RegionsAttributeConverter {

    public static final String convertToDatabaseColumn(Regions regions) {
        if (regions.size() == 0){
            return null;
        }
        String res = regions.toString();
        return res;
    }
    public static final Regions convertToEntityAttribute(String str) {
        if ( str == null ) {
            return new Regions();
        }
        System.out.println("Str로 들어온 값: " + str);
        List<String> strings = Arrays.asList(str.substring(1, str.length()-1).split(", "));
        System.out.println("Strs는 : " + strings);
        return new Regions( strings.stream().map(e -> Region.deserialize(e)).collect(Collectors.toList()) );
    }
}
