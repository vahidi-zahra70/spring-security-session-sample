package com.pracetice.spring_security_session.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping
    public String getUser(HttpServletRequest request) {
        log.info(">>>>>>>>>>>>>>>>>> {}", SecurityContextHolder.getContext().getAuthentication().getName());
        HttpSession session = request.getSession(false);

        //list all attributes
        session.getAttributeNames();
        SecurityContext securityContext = session == null
                ? null
                : (SecurityContext) session.getAttribute(
                HttpSessionSecurityContextRepository
                        .SPRING_SECURITY_CONTEXT_KEY
        );

        Authentication authentication = securityContext == null
                ? null
                : securityContext.getAuthentication();

        if (authentication != null) {
            System.out.println(authentication.getPrincipal());
            System.out.println(authentication.getName());
            System.out.println(authentication.isAuthenticated());
            System.out.println(authentication.getAuthorities());
        }

        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    }

    @PostMapping
    public void setUser() {
        return;
    }
}
