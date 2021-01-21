package com.api.yirang.auth.presentation.config;

import com.api.yirang.auth.application.intermediateService.JwtUserDetailsService;
import com.api.yirang.auth.domain.jwt.components.JwtValidator;
import com.api.yirang.auth.presentation.errorEntryPoint.RestAuthorizationEntryPoint;
import com.api.yirang.auth.presentation.filter.JwtAuthenticationFilter;
import com.api.yirang.auth.presentation.errorEntryPoint.RestAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Profile("dev")
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

    // Swagger 를 위해서
    @Override
    public void configure(WebSecurity web) throws Exception{
        super.configure(web);
        web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources",
                                   "/configuration/security", "/swagger-ui.html", "/webjars/**","/swagger/**",
                                   "/swagger-resources/**", "/csrf");
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
            /** swagger UI */
            .antMatchers("/api-ui.html", "/swagger-ui/**", "/swagger-ui.html", "/v2/api-docs", "/api-ui").permitAll()
            /** Sign In & Token **/
            .antMatchers("/v1/apis/auth/signin", "/v1/apis/auth/signin/**").permitAll()
            .antMatchers("/v1/apis/auth/refresh").hasAnyAuthority("VOLUNTEER", "ADMIN", "SUPER_ADMIN")
            /** 이스터 에그 **/
            .antMatchers("/v1/apis/admins").hasAnyAuthority("VOLUNTEER", "ADMIN")
            /** Admin Region */
            .antMatchers("/v1/apis/admins/region","/v1/apis/admins/region/**").hasAnyAuthority("ADMIN", "SUPER_ADMIN")
            /** Seniors */
            .antMatchers("/v1/apis/seniors", "/v1/apis/seniors/**").hasAnyAuthority("ADMIN", "SUPER_ADMIN")
            /** Apply */
            // 자신의 Apply 조회 및 신청 하는 건 Volunteer 만 가능하다.
            .antMatchers(HttpMethod.GET, "/v1/apis/apply", "/v1/apis/apply/apply-check/notices/**").hasAuthority("VOLUNTEER")
            .antMatchers(HttpMethod.POST, "/v1/apis/apply/notices").hasAuthority("VOLUNTEER")
            // Apply 삭제는 관리자와 봉사자 모두 가능하다.
            .antMatchers(HttpMethod.DELETE, "/v1/apis/apply/**").hasAnyAuthority("VOLUNTEER", "ADMIN", "SUPER_ADMIN")
            // 해당 게시물을 신청한 봉사자 정보 얻기
            .antMatchers(HttpMethod.GET, "/v1/apis/apply/notices/**").hasAnyAuthority("ADMIN", "SUPER_ADMIN")
            /** INFO */
            // 다른 유저들의 정보 조회나 Kick은 Super Admin만
            .antMatchers("/v1/apis/info/users/**").hasAuthority("SUPER_ADMIN")
            // 자신의 Information 보는건 AnonyMous가 아니면 다 가능하다
            .antMatchers("/v1/apis/info/myinfo").hasAnyAuthority("ADMIN", "VOLUNTEER", "SUPER_ADMIN")
            /** Notice **/
            // 공고글 보는 건 Anonymous, User, admin 다 가능 해야함
            .antMatchers(HttpMethod.GET, "/v1/apis/manage/notices", "/v1/apis/manage/notices/**").permitAll()
            .antMatchers("/v1/apis/manage/notices", "/v1/apis/manage/notices/**").hasAnyAuthority("ADMIN", "SUPER_ADMIN")
            .anyRequest().authenticated()
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(restAuthenticationEntryPoint)
            .accessDeniedHandler(restAuthorizationEntryPoint)
            .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
