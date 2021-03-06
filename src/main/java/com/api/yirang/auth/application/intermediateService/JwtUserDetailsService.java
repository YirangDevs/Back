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
public class JwtUserDetailsService implements UserDetailsService {

    // DI jwt
    private final JwtParser jwtParser;
    private final UserService userService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String YAT) throws UsernameNotFoundException {

        Long userId = jwtParser.getUserIdFromJwt(YAT);
        String username = "1234";
        String password = "1234";
        // user ID를 DB에서 검사
        Authority authority = userService.getAuthorityByUserId(userId);

        UserDetailsVO userDetailsVO = UserDetailsVO.builder()
                                                   .userId(userId).username(username)
                                                   .password(password).authority(authority)
                                                   .build();

        System.out.println("[UserDetailService]: UserDetail 생성: " + userDetailsVO);

        return userDetailsVO;
    }
}
