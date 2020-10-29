package com.api.yirang.common.domain.region.exception;


import com.api.yirang.common.exceptions.ApiException;

public class AlreadyExistedRegion extends ApiException {

    public AlreadyExistedRegion() {
        super("133", "Already Existed Region");
    }
}
