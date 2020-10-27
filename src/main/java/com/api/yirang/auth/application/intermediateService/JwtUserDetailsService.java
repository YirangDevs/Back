package com.api.yirang.auth.application.intermediateService;

import com.api.yirang.auth.domain.jwt.components.JwtParser;
import com.api.yirang.auth.domain.jwt.components.JwtValidator;
import com.api.yirang.auth.presentation.VO.UserDetailsVO;
import com.api.yirang.auth.support.type.Authority;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    // DI jwt
    private final JwtValidator jwtValidator;
    private final JwtParser jwtParser;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String YAT) throws UsernameNotFoundException {
        jwtValidator.isValidJwt(YAT);

        Long userId = jwtParser.getUserIdFromJwt(YAT);
        String username = jwtParser.getUsernameFromJwt(YAT);
        String password = "1234";
        Authority authority = jwtParser.getRoleFromJwt(YAT);

        return UserDetailsVO.builder()
                            .userId(userId)
                            .username(username)
                            .password(password)
                            .authority(authority)
                            .build();
    }
}
