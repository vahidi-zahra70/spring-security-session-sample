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
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import java.io.IOException;

public class MyAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public MyAuthenticationFilter(AuthenticationManager authenticationManager) {
        setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl("/login");

        setSecurityContextRepository(new HttpSessionSecurityContextRepository());

        setAuthenticationSuccessHandler((request, response, authentication) -> {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("Login successful!");
            response.addCookie(new Cookie("LoggedIn", "true"));
        });

        setAuthenticationFailureHandler((request, response, exception) -> {
            System.out.println(">>> Authentication failed: " + exception.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Login failed!");
        });
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult)
            throws IOException, ServletException {

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        super.successfulAuthentication(request, response, chain, authResult);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>> here in filter");
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
