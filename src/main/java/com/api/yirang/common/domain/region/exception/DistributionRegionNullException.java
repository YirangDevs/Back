package com.api.yirang.common.domain.region.exception;

import com.api.yirang.common.exceptions.ApiException;

import java.util.function.Supplier;

public class DistributionRegionNullException extends ApiException implements Supplier<DistributionRegionNullException> {

    public DistributionRegionNullException() {
        super("014", "DistributionRegion Not Found");
    }

    @Override
    public DistributionRegionNullException get() {
        return this;
    }
}
