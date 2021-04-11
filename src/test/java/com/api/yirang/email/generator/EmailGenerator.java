package com.api.yirang.email.generator;

import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.generator.UserGenerator;
import com.api.yirang.email.model.Email;

public class EmailGenerator {

    public static Email createRandomEmail(User user){

        return Email.builder()
                    .user(user)
                    .build();
    }

    public static Email createRandomEmail(){

        User user = UserGenerator.createRandomUser();

        return Email.builder()
                    .user(user)
                    .build();
    }

}
