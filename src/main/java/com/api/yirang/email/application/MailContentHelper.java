package com.api.yirang.email.application;

import com.api.yirang.auth.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@RequiredArgsConstructor
public class MailContentHelper {

    private final TemplateEngine templateEngine;

    public String generateVerifyMailContent(User user, String certificationNumbers){
        // 사용자의 realname이 기본 없으면 username으로
        String name = user.getRealname() != null ? user.getRealname() : user.getUsername();

        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("certificationNumbers", certificationNumbers);

        return templateEngine.process("verifyMail", context);

    }

}
