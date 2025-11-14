package com.pracetice.spring_security_session.filter;

import com.pracetice.spring_security_session.dto.MyAuthentication;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.ChangeSessionIdAuthenticationStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class MyAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public MyAuthenticationFilter(AuthenticationManager authenticationManager) {
        setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl("/login");

        setSessionAuthenticationStrategy(new ChangeSessionIdAuthenticationStrategy());


        setSecurityContextRepository(new HttpSessionSecurityContextRepository());

        setAuthenticationSuccessHandler((request, response, authentication) -> {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("Login successful!");

            Cookie cookie = new Cookie("LoggedIn", "true");
            cookie.setSecure(false);
            cookie.setHttpOnly(false);
            cookie.setPath("/");
            cookie.setMaxAge((int) Duration.of(10, ChronoUnit.MINUTES).getSeconds());

            response.addCookie(cookie);
        });

        setAuthenticationFailureHandler((request, response, exception) -> {
            System.out.println(">>> Authentication failed: " + exception.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Login failed!");
        });
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println(">>>>>>>>>>>>>>>>> in my filter");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String securityAnswer = request.getParameter("security_answer");


        MyAuthentication myAuthentication = MyAuthentication.builder()
                .username(username)
                .password(password)
                .securityAnswer(securityAnswer).build();

        return this.getAuthenticationManager().authenticate(myAuthentication);
    }


}
