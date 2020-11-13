package com.api.yirang.common.domain.region.exception;

import com.api.yirang.common.exceptions.ApiException;

public class AlreadyExistedDistribution extends ApiException {

    public AlreadyExistedDistribution() {
        super("133", "Already Existed Distribution");
    }
}
