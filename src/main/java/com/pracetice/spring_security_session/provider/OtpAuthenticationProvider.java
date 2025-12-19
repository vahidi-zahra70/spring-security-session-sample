package com.pracetice.spring_security_session.provider;

import com.pracetice.spring_security_session.dto.OtpAuthentication;
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
public class OtpAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OtpAuthentication otpAuthentication;
        String otp= String.valueOf(authentication.getCredentials());
        if(otp.equals("3344")) {
           otpAuthentication= new OtpAuthentication(authentication.getName(),otp, List.of(new SimpleGrantedAuthority("Role_user")));
        }
        else{
            throw new BadCredentialsException("otp is wrong");
        }

        return otpAuthentication;
    }

    @Override
    public boolean supports(Class<?> authenticationType) {
        return authenticationType.equals(OtpAuthentication.class);
    }
}
