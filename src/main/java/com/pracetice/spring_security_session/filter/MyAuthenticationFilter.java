package com.pracetice.spring_security_session.filter;

import com.pracetice.spring_security_session.dto.OtpAuthentication;
import com.pracetice.spring_security_session.dto.UserNamePasswordAuthentication;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.ChangeSessionIdAuthenticationStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.util.StringUtils;

public class MyAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public MyAuthenticationFilter(AuthenticationManager authenticationManager) {
        setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl("/login");

        setSessionAuthenticationStrategy(new ChangeSessionIdAuthenticationStrategy());


        setSecurityContextRepository(new HttpSessionSecurityContextRepository());

        setAuthenticationSuccessHandler((request, response, authentication) -> {
            if(authentication.isAuthenticated()) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("Login successful!");

                Cookie cookie = new Cookie("LoggedIn", "true");
                cookie.setSecure(false);
                cookie.setHttpOnly(false);
                cookie.setPath("/");
//            cookie.setMaxAge((int) Duration.of(10, ChronoUnit.MINUTES).getSeconds());

                response.addCookie(cookie);
            }
            else{
                response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
                response.getWriter().write("Otp is sent!");
            }
        });

        setAuthenticationFailureHandler((request, response, exception) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(exception.getMessage());
        });
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println(">>>>>>>>>>>>>>>>> in my filter");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String securityAnswer = request.getParameter("security_answer");

        Authentication authentication;
        if (!StringUtils.hasText(securityAnswer)) {
            authentication= new UserNamePasswordAuthentication(username, password);
        } else {
            authentication= new OtpAuthentication(username, securityAnswer);
        }

        return this.getAuthenticationManager().authenticate(authentication);
    }


}
