package com.api.yirang.common.support.attributeConverter;

import com.api.yirang.common.support.type.Region;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

public class RegionAttributeConverter implements AttributeConverter<Region, String> {

    @Override
    public String convertToDatabaseColumn(Region region) {
        return region == null ? null : region.serialize();
    }

    @Override
    public Region convertToEntityAttribute(String value) {
        return value == null ? null : Region.deserialize(value);
    }
}
