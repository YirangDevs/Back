package com.api.yirang.auth.domain.user.exceptions;

import com.api.yirang.common.exceptions.AlreadyException;

public class AlreadyExistedVolunteer extends AlreadyException {
    public AlreadyExistedVolunteer(){
        super("Volunteer Already Exist!");
    }
}
