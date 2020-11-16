package com.api.yirang.common.support.attributeConverter;

import com.api.yirang.common.support.type.Sex;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class SexAttributeConverter implements AttributeConverter<Sex, String> {

    @Override
    public String convertToDatabaseColumn(Sex sex) {
        return sex == null ? null : sex.serialize();
    }

    @Override
    public Sex convertToEntityAttribute(String value) {
        return value == null ? null : Sex.deserialize(value);
    }
}
