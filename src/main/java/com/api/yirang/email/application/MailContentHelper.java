package com.api.yirang.email.application;

import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.email.model.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@RequiredArgsConstructor
public class MailContentHelper {

    private final TemplateEngine templateEngine;

    public String generateVerifyMailContent(User user, String certificationNumbers){
        // User 이름
        String name = user.getUsername();

        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("certificationNumbers", certificationNumbers);

        return templateEngine.process("verifyMail", context);

    }

}
