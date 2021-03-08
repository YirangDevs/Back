package com.api.yirang.email.exception;

import com.api.yirang.common.exceptions.UtilException;

public class EmailCertificationFailException extends UtilException {
    public EmailCertificationFailException() {
        super("Email Certification is not same as What you given");
    }
}
