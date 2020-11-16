package com.api.yirang.common.support.converter;

import com.api.yirang.common.support.type.Region;
import org.springframework.core.convert.converter.Converter;

public class String2RegionConverter implements Converter<String, Region> {
    @Override
    public Region convert(String value) {
        return Region.deserialize(value);
    }

}
