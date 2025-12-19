package com.pracetice.spring_security_session.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

@Setter
@Getter
public class UserNamePasswordAuthentication extends UsernamePasswordAuthenticationToken implements Serializable {

    public UserNamePasswordAuthentication(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public UserNamePasswordAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}

