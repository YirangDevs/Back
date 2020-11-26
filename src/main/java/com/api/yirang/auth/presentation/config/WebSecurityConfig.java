package com.api.yirang.auth.presentation.config;

import com.api.yirang.auth.application.intermediateService.JwtUserDetailsService;
import com.api.yirang.auth.domain.jwt.components.JwtValidator;
import com.api.yirang.auth.presentation.errorEntryPoint.RestAuthorizationEntryPoint;
import com.api.yirang.auth.presentation.filter.JwtAuthenticationFilter;
import com.api.yirang.auth.presentation.errorEntryPoint.RestAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    // DI EntryPoint
    @Autowired
    RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    @Autowired
    RestAuthorizationEntryPoint restAuthorizationEntryPoint;

    // DI UserDetailService
    @Autowired
    JwtUserDetailsService jwtUserDetailsService;

    // DI jwt
    @Autowired
    JwtValidator jwtValidator;

    @Bean
    public JwtAuthenticationFilter authenticationFilter(){
        return new JwtAuthenticationFilter(jwtUserDetailsService, jwtValidator);
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{

        httpSecurity
            .cors().and()
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/v1/apis/auth/signin").permitAll()
            .antMatchers("/v1/apis/test/**").permitAll()
            // h2-conosole
            .antMatchers("/h2-console", "/h2", "/h2/").permitAll()
            // 임시로..
            .antMatchers("/v1/apis/admins").hasAnyAuthority("VOLUNTEER", "ADMIN")
            .antMatchers("/v1/apis/admins/region/**").hasAnyAuthority("ADMIN")
            .antMatchers("/v1/apis/auth/refresh").hasAnyAuthority("VOLUNTEER", "ADMIN")
            .antMatchers("/v1/apis/seniors", "/v1/apis/seniors/**").hasAnyAuthority("ADMIN")
            // 공고글 보는 건 Anonymous, User, admin 다 가능 해야함
            .antMatchers(HttpMethod.GET, "/v1/apis/manage/notices", "/v1/apis/manage/notices/**").permitAll()
            .antMatchers("/v1/apis/manage/notices", "/v1/apis/manage/notices/**").hasAuthority("ADMIN")
            .anyRequest().authenticated()
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(restAuthenticationEntryPoint)
            .accessDeniedHandler(restAuthorizationEntryPoint)
            .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        // h2-console 위해서 설정
    }
}
