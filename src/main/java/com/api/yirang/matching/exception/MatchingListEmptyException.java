package com.api.yirang.matching.exception;

import com.api.yirang.common.exceptions.NullException;

public class MatchingListEmptyException extends NullException {

    public MatchingListEmptyException() {
        super("MatchingList is empty!");
    }
}
