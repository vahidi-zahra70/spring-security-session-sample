package com.pracetice.spring_security_session.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

@Setter
@Getter
@Builder
public class MyAuthentication implements Authentication , Serializable {

    private String username;
    private String password;
    private String securityAnswer;
    private String nationalCode;
    private boolean authenticated;
    private Collection<? extends GrantedAuthority> authorities;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public String getPrincipal() {
        return username;
    }


    @Override
    public String getName() {
        return null;
    }
}
