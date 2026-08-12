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
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.util.StringUtils;

public class MyAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final int CSRF_COOKIE_MAX_AGE_SECONDS = 10 * 60;

    public MyAuthenticationFilter(AuthenticationManager authenticationManager,
                                  SessionAuthenticationStrategy sessionAuthenticationStrategy,
                                  CsrfTokenRepository csrfTokenRepository, AuthenticationEntryPoint authenticationEntryPoint) {
        setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl("/login");

        setSessionAuthenticationStrategy(sessionAuthenticationStrategy);

        setSecurityContextRepository(new HttpSessionSecurityContextRepository());

        setAuthenticationSuccessHandler((request, response, authentication) -> {
            // This handler is reached only for the fully authenticated OTP step.
            // Authoritative copy: stored as a Spring Session attribute in Redis.
            CsrfToken csrfToken = csrfTokenRepository.generateToken(request);
            csrfTokenRepository.saveToken(csrfToken, request, response);

            Cookie csrfCookie = new Cookie("XSRF-TOKEN", csrfToken.getToken());
            csrfCookie.setPath("/");
            csrfCookie.setMaxAge(CSRF_COOKIE_MAX_AGE_SECONDS);
            csrfCookie.setHttpOnly(false);
            csrfCookie.setSecure(request.isSecure());
            csrfCookie.setAttribute("SameSite", "Lax");
            response.addCookie(csrfCookie);

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

//    @Override
//    protected void successfulAuthentication(HttpServletRequest request,
//                                            HttpServletResponse response,
//                                            FilterChain chain,
//                                            Authentication authentication)
//            throws IOException, ServletException {
//
//        if (!authentication.isAuthenticated()) {
//            // The username/password step only starts the OTP challenge. Do not
//            // call super, because it would save this incomplete Authentication
//            // in SPRING_SECURITY_CONTEXT and make it look non-anonymous later.
//            response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
//            response.getWriter().write("Otp is sent!");
//            return;
//        }
//
//        super.successfulAuthentication(request, response, chain, authentication);
//    }
}
