package com.api.yirang.auth.presentation.errorEntryPoint;

import com.api.yirang.auth.domain.jwt.exceptions.InvalidJwtException;
import com.api.yirang.auth.domain.jwt.exceptions.JwtTokenMissingException;
import com.api.yirang.common.exceptions.Dto.ErrorDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {

        System.out.println("[RestAuthenticationEntryPoint]: Authentication is failed");

        ErrorDto errorDto = ErrorDto.builder()
                                    .errorCode("012")
                                    .errorName("Invalid Jwt or Missing Jwt")
                                    .build();
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        OutputStream out = httpServletResponse.getOutputStream();

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, errorDto);
        out.flush();
    }
}
