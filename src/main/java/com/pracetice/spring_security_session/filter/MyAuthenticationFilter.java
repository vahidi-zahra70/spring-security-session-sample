package com.pracetice.spring_security_session.filter;

import com.pracetice.spring_security_session.dto.OtpAuthentication;
import com.pracetice.spring_security_session.dto.UserNamePasswordAuthentication;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.ChangeSessionIdAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.util.StringUtils;

public class MyAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    public MyAuthenticationFilter(AuthenticationManager authenticationManager,
                                 AuthenticationEntryPoint authenticationEntryPoint) {
        setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl("/login");

        setSessionAuthenticationStrategy(new ChangeSessionIdAuthenticationStrategy());

        setSecurityContextRepository(new HttpSessionSecurityContextRepository());

        setAuthenticationSuccessHandler((request, response, authentication) -> {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("Login successful!");
        });

        setAuthenticationFailureHandler(authenticationEntryPoint::commence);
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String securityAnswer = request.getParameter("security_answer");

        Authentication authentication;
        if (!StringUtils.hasText(securityAnswer)) {
            authentication = new UserNamePasswordAuthentication(username, password);
        } else {
            authentication = new OtpAuthentication(username, securityAnswer);
        }

        authentication = this.getAuthenticationManager().authenticate(authentication);

        if (authentication.isAuthenticated()) {
            return authentication;
        }

        // A null result tells AbstractAuthenticationProcessingFilter that this
        // authentication flow is not complete. It will return immediately and
        // will not invoke either the success or failure handler, so this step
        // must produce its own response.
        response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
        response.setContentType("text/plain");
        try {
            response.getWriter().write("Otp is sent!");
        }
        catch (java.io.IOException exception) {
            throw new org.springframework.security.authentication.InternalAuthenticationServiceException(
                    "Could not write the OTP challenge response", exception);
        }
        return null;
    }

}
