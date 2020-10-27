package com.api.yirang.auth.presentation.VO;

import com.api.yirang.auth.support.type.Authority;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@ToString
public class UserDetailsVO implements UserDetails {

    private static final long serialVersionUID = 1L;

    private final Long userId;
    private final String username;

    private final Collection<? extends GrantedAuthority> authorities;

    @JsonIgnore
    private final String password;

    public UserDetailsVO() {
        this.userId = null;
        this.username = null;
        this.password = null;
        this.authorities = null;
    }

    @Builder
    public UserDetailsVO(Long userId, String username, Authority authority, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(authority.toString()));

        this.authorities = authorities;

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public Long getUserId(){
        return userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsVO user = (UserDetailsVO) o;
        return Objects.equals(userId, user.userId);
    }
}
