package com.api.yirang.auth.presentation.errorEntryPoint;

import com.api.yirang.common.exceptions.Dto.ErrorDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
public class RestAuthorizationEntryPoint implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {

        System.out.println("[RestAuthorizationEntryPoint]: Access failed");

        ErrorDto errorDto = ErrorDto.builder()
                                    .errorCode("123")
                                    .errorName("Has no Authority")
                                    .build();
        
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        OutputStream out = httpServletResponse.getOutputStream();

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, errorDto);
        out.flush();
    }
}
