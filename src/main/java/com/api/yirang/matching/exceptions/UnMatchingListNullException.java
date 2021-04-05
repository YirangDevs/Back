package com.api.yirang.matching.exceptions;

import com.api.yirang.common.exceptions.NullException;

public class UnMatchingListNullException extends NullException {

    public UnMatchingListNullException() {
        super("There is no UnMatchingList");
    }
}
