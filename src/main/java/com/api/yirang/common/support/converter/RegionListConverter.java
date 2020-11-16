package com.api.yirang.common.support.converter;

import com.api.yirang.common.support.type.Region;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RegionListConverter {

    public static final String convertFromListToString(List<Region> regions) {
        if (regions == null || regions.size() == 0){
            return "";
        }
        String res = regions.toString();
        return res;
    }
    public static final List<Region> convertFromStringToList(String str) {
        if ( str == null || str.equals("")) {
            return new ArrayList<>();
        }
        List<String> strings = Arrays.asList(str.substring(1, str.length()-1).split(", "));
        return strings.stream().map(e -> Region.deserialize(e)).collect(Collectors.toList());
    }
}
