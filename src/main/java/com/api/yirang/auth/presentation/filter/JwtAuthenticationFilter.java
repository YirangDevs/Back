//package com.api.yirang.auth.presentation.filter;
//
//import com.api.yirang.auth.application.basicService.UserService;
//import com.api.yirang.auth.domain.jwt.components.JwtParser;
//import com.api.yirang.auth.domain.jwt.components.JwtValidator;
//import com.api.yirang.auth.domain.jwt.exceptions.JwtTokenMissingException;
//import com.api.yirang.auth.support.type.Authority;
//import com.api.yirang.auth.support.utils.ParsingHelper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.security.web.util.matcher.RequestMatcher;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//
//@Component
//@RequiredArgsConstructor
//public class JwtAuthenticationFilter extends OncePerRequestFilter{
//
//    // DI for services
//    private final JwtUserDetailService jwtUserDetailService;
//    private final UserService userService;
//
//    // DI for Jwt
//    private final JwtParser jwtParser;
//    private final JwtValidator jwtValidator;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
//        final String requestTokenHeader = httpServletRequest.getHeader("Authorization");
//
//        if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer ")){
//            throw new JwtTokenMissingException();
//        }
//
//        String yat = ParsingHelper.parseHeader(requestTokenHeader);
//
//        Long userId = jwtParser.getUserIdFromJwt(yat);
//        Authority role = jwtParser.getRoleFromJwt(yat);
//
//        UserDetails userDetails = jwtUserDetailService.loadUserByUserId(userId);
//
//        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
//                                                = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities() );
//        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
//        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//        filterChain.doFilter(httpServletRequest, httpServletResponse);
//    }
//}
