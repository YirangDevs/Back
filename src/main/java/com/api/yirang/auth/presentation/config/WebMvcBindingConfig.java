package com.api.yirang.auth.presentation.config;

import com.api.yirang.common.support.converter.String2RegionConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebMvcBindingConfig extends WebMvcConfigurationSupport {

    @Override
    protected void addFormatters(FormatterRegistry registry){
        registry.addConverter(new String2RegionConverter());
    }
}
