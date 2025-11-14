package com.pracetice.spring_security_session.config;

import com.pracetice.spring_security_session.dto.MyAuthentication;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class MyAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        MyAuthentication myAuthentication= (MyAuthentication) authentication;
        myAuthentication.setAuthenticated(true);
        myAuthentication.setNationalCode("0013759388");
        myAuthentication.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        return myAuthentication;
    }

    @Override
    public boolean supports(Class<?> authenticationType) {
        return authenticationType.equals(MyAuthentication.class);
    }
}
