package com.pracetice.spring_security_session.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

@Setter
@Getter
public class OtpAuthentication extends UsernamePasswordAuthenticationToken implements Serializable {

    private String csrfToken;

    public OtpAuthentication(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public OtpAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.csrfToken= UUID.randomUUID().toString();
    }
}


