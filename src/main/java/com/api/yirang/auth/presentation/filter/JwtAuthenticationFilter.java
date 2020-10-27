package com.api.yirang.auth.presentation.filter;

import com.api.yirang.auth.application.intermediateService.JwtUserDetailsService;
import com.api.yirang.auth.domain.jwt.components.JwtParser;
import com.api.yirang.auth.domain.jwt.components.JwtValidator;
import com.api.yirang.auth.domain.jwt.exceptions.JwtTokenMissingException;
import com.api.yirang.auth.support.type.Authority;
import com.api.yirang.auth.support.utils.ParsingHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // DI service
    private final JwtUserDetailsService jwtUserDetailsService;
    // DI for Jwt
    private final JwtValidator jwtValidator;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader = httpServletRequest.getHeader("Authorization");

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")){
            System.out.println("[Filter] Yat가 있습니다. Yat를 검사하겠습니다.");
            String yat = ParsingHelper.parseHeader(requestTokenHeader);
            jwtValidator.isValidJwt(yat);
            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(yat);
            UsernamePasswordAuthenticationToken authentication
                    = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities() );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("[Filter] 인증, 인가가 완료되었습니다.");
        }
        else{
            System.out.println("[Filter] Yat가 없습니다..");
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

}
