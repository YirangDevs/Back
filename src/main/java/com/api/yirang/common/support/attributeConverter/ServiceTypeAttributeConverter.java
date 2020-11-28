package com.api.yirang.common.support.attributeConverter;

import com.api.yirang.seniors.support.custom.ServiceType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Created by JeongminYoo on 2020/11/28
 * Work with Yirang
 * Email: likemin0142@gmail.com
 * Blog: http://Beewoom.github.io
 * Github: http://github.com/Biewoom
 */
@Converter(autoApply = true)
public class ServiceTypeAttributeConverter implements AttributeConverter<ServiceType, String> {

    @Override
    public String convertToDatabaseColumn(ServiceType serviceType) {
        return serviceType == null ? null : serviceType.serialize();
    }

    @Override
    public ServiceType convertToEntityAttribute(String value) {
        return value == null ? null : ServiceType.deserialize(value);
    }
}
