package com.pracetice.spring_security_session.config;

import com.pracetice.spring_security_session.dto.UserNamePasswordAuthentication;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        UserNamePasswordAuthentication userNamePasswordAuthentication;
//        if(StringUtils.hasText(authentication.getName())) {
//            throw new BadCredentialsException("otp is sent");
//        }
//        else{
//            throw new BadCredentialsException("username is empty");
//        }
//
//    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserNamePasswordAuthentication userNamePasswordAuthentication=null;
        if(StringUtils.hasText(authentication.getName())) {
            userNamePasswordAuthentication=new UserNamePasswordAuthentication(authentication.getName(),authentication.getCredentials(), List.of(new SimpleGrantedAuthority("Role_user")));
        }
        else{
            throw new BadCredentialsException("username is empty");
        }

        return userNamePasswordAuthentication;
    }

    @Override
    public boolean supports(Class<?> authenticationType) {
        return authenticationType.equals(UserNamePasswordAuthentication.class);
    }
}
