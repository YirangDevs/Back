package com.api.yirang.matching.exceptions;

import com.api.yirang.common.exceptions.NullException;

public class MatchingNullException extends NullException {

    public MatchingNullException() {
        super("Required Matching does not exist");
    }
}
