package com.api.yirang.common.domain.region.exception;

import com.api.yirang.common.exceptions.ApiException;

import java.util.function.Supplier;

public class RegionNullException extends ApiException implements Supplier<RegionNullException> {


    public RegionNullException() {
        super("072", "Region Not Found");
    }

    @Override
    public RegionNullException get() {
        return this;
    }
}
